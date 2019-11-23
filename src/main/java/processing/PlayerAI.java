package processing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerAI extends Player implements AI{

    private Point skippingPoint;
    private int depth;

    public PlayerAI(Side s, int depth) {
        super(s);
        this.depth = depth;
    }

    /**
     * Mave a move on the board
     * @param board the concerned board
     * @return the decision after performing the move
     */
    public Board.Decision makeMove(Board board) {

        //Compute the best move with the MiniMax algorithm
        Move m = nextMove(board, depth, getSide(), true);

        Board.Decision decision = board.makeMove(m, getSide());

        if(decision == Board.Decision.ADDITIONAL_MOVE) {
            assert m != null;
            skippingPoint = m.getEnd();
        }
        return decision;
    }

    /**
     * compute the best next move to play using minimax algorithm with alpha beta pruning
     * @param board the concerned board
     * @param depth minimax algorithm depth
     * @param side AI board side
     * @param maximizingPlayer  determines if Player is maximizing or minimizing (Here the AI is always maximizing)
     * @return the computed next move to play
     */
    private Move nextMove(Board board, int depth, Side side, boolean maximizingPlayer) {

        //Prunning elements
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        List<Move> possibleMoves;
        if(skippingPoint == null)
            possibleMoves = board.getAllValidMoves(side);
        else
        {
            possibleMoves = board.getValidSkipMoves(skippingPoint.x, skippingPoint.y, side);
            skippingPoint = null;
        }

        List<Double> heuristics = new ArrayList<>();
        if(possibleMoves.isEmpty())
            return null;
        Board tempBoard;

        //Compute heuristic for all possible moves
        for (Move possibleMove : possibleMoves) {
            tempBoard = board.clone();
            tempBoard.makeMove(possibleMove, side);
            heuristics.add(minimax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta));
        }

        double maxHeuristics = Double.NEGATIVE_INFINITY;

        Random rand = new Random();
        //Searching for the max heuristics
        for(int i = heuristics.size() - 1; i >= 0; i--) {
            if (heuristics.get(i) >= maxHeuristics) {
                maxHeuristics = heuristics.get(i);
            }
        }

        //Filtering heuristics to keep all max values
        for(int i = 0; i < heuristics.size(); i++)
        {
            if(heuristics.get(i) < maxHeuristics)
            {
                heuristics.remove(i);
                possibleMoves.remove(i);
                i--;
            }
        }

        //Choose a random move with max heuristic value
        return possibleMoves.get(rand.nextInt(possibleMoves.size()));
    }

    private double minimax(Board board, int depth, Side side, boolean maximizingPlayer, double alpha, double beta) {
        // Return board heuristic when we are the end of exploration
        if(depth == 0) {
            return getHeuristic(board);
        }
        List<Move> possibleMoves = board.getAllValidMoves(side);

        double initial = 0;
        Board tempBoard;
        //maximizing
        if(maximizingPlayer)
        {
            initial = Double.NEGATIVE_INFINITY;
            for (Move possibleMove : possibleMoves) {
                tempBoard = board.clone();
                tempBoard.makeMove(possibleMove, side);

                double result = minimax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta);

                initial = Math.max(result, initial);
                alpha = Math.max(alpha, initial);

                //Pruning
                if (alpha >= beta)
                    break;
            }
        }
        //minimizing
        else
        {
            initial = Double.POSITIVE_INFINITY;

            for (Move possibleMove : possibleMoves) {
                tempBoard = board.clone();
                tempBoard.makeMove(possibleMove, side);

                double result = minimax(tempBoard, depth - 1, flipSide(side), !maximizingPlayer, alpha, beta);

                initial = Math.min(result, initial);
                alpha = Math.min(alpha, initial);

                //Pruning
                if (alpha >= beta)
                    break;
            }
        }

        return initial;
    }

    /**
     * Heuristic implementation [May be modified for tests or for fitting your needs]
     * @param b the board state
     * @return the computed heuristic
     */
    private double getHeuristic(Board b) {
        double kingWeight = 1.2;
        double result;
        if(getSide() == Side.WHITE)
            result = b.getNumWhiteKingPieces() * kingWeight + b.getNumWhiteNormalPieces() - b.getNumBlackKingPieces() *
                    kingWeight -
                    b.getNumBlackNormalPieces();
        else
            result = b.getNumBlackKingPieces() * kingWeight + b.getNumBlackNormalPieces() - b.getNumWhiteKingPieces() *
                    kingWeight -
                    b.getNumWhiteNormalPieces();
        return result;

    }

    /**
     * Flip side
     * @param side the current side
     * @return the opposite side
     */
    private Side flipSide(Side side) {
        if(side == Side.BLACK)
            return Side.WHITE;
        return Side.BLACK;
    }
}

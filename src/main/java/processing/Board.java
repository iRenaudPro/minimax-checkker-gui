package processing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Board {

    private Type[][] board;
    public final int SIZE = 8;

    private int numWhiteNormalPieces;
    private int numBlackNormalPieces;
    private int numBlackKingPieces;
    private int numWhiteKingPieces;

    public enum Type {
        EMPTY, WHITE, BLACK, WHITE_KING, BLACK_KING
    }

    public enum Decision {
        COMPLETED,
        FAILED_MOVING_INVALID_PIECE,
        FAILED_INVALID_DESTINATION,
        ADDITIONAL_MOVE,
        GAME_ENDED,
        JUMP_AVAILABLE
    }

    public Board() {
        setUpBoard();
    }

    private Board(Type[][] board)
    {
        numWhiteNormalPieces = 0;
        numBlackNormalPieces = 0;
        numBlackKingPieces = 0;
        numWhiteKingPieces = 0;

        this.board = board;
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j< SIZE; j++)
            {
                Type piece = getPiece(i, j);
                if(piece == Type.BLACK)
                    numBlackNormalPieces++;
                else if(piece == Type.BLACK_KING)
                    numBlackKingPieces++;
                else if(piece == Type.WHITE)
                    numWhiteNormalPieces++;
                else if(piece == Type.WHITE_KING)
                    numWhiteKingPieces++;
            }
        }
    }

    private void setUpBoard() {
        numWhiteNormalPieces = 12;
        numBlackNormalPieces = 12;
        numBlackKingPieces = 0;
        numWhiteKingPieces = 0;
        board = new Type[SIZE][SIZE];
        for (int i = 0; i < board.length; i++) {
            int start = 0;
            if (i % 2 == 0)
                start = 1;

            Type pieceType = Type.EMPTY;
            if (i <= 2)
                pieceType = Type.WHITE;
            else if (i >= 5)
                pieceType = Type.BLACK;

            for (int j = start; j < board[i].length; j += 2) {
                board[i][j] = pieceType;
            }
        }

        populateEmptyOnBoard();
    }

    private void populateEmptyOnBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null)
                    board[i][j] = Type.EMPTY;
            }
        }
    }

    private Type getPiece(int row, int col) {
        return board[row][col];
    }

    private Type getPiece(Point point) {
        return board[point.x][point.y];
    }

    public Type[][] getBoard()
    {
        return board;
    }

    public int getNumWhitePieces() {
        return numWhiteKingPieces + numWhiteNormalPieces;
    }

    public int getNumBlackPieces() {
        return numBlackKingPieces + numBlackNormalPieces;
    }

    int getNumWhiteKingPieces()
    {
        return numWhiteKingPieces;
    }
    int getNumBlackKingPieces()
    {
        return numBlackKingPieces;
    }
    int getNumWhiteNormalPieces()
    {
        return numWhiteNormalPieces;
    }
    int getNumBlackNormalPieces()
    {
        return numBlackNormalPieces;
    }

    /**
     * Makes a move and return the decision
     * @param move the move to perform
     * @param side the side on which the move must be performed
     * @return the decision after move evaluation
     */
    public Decision makeMove(Move move, Player.Side side) {
        if(move == null) {
            return Decision.GAME_ENDED;
        }

        Point start = move.getStart();
        int startRow = start.x;
        int startCol = start.y;
        Point end = move.getEnd();
        int endRow = end.x;
        int endCol = end.y;

        //can only move own piece and not empty space
        if (!isMovingOwnPiece(startRow, startCol, side) || getPiece(startRow, startCol) == Type.EMPTY)
            return Decision.FAILED_MOVING_INVALID_PIECE;

        List<Move> possibleMoves = getValidMoves(startRow, startCol, side);
        List<Move> allValidMoves = getAllValidMoves(side);

        //Check if jump move is available
        boolean jumpMoveAvailable = false;
        for(Move m: allValidMoves){
            if(m.getStart().x+1 != m.getEnd().x && m.getStart().x-1 != m.getEnd().x){
                jumpMoveAvailable = true;
                break;
            }
        }

        Type currType = getPiece(startRow, startCol);

        if (possibleMoves.contains(move)) {
            boolean jumpMove = false;
            //if it contains move then it is either 1 move or 1 jump
            if (startRow + 1 == endRow || startRow - 1 == endRow) {

                //If the selected move is not a jump when jump move is available
                if(jumpMoveAvailable){
                    return Decision.JUMP_AVAILABLE;
                }


                board[startRow][startCol] = Type.EMPTY;
                board[endRow][endCol] = currType;


            } else {
                jumpMove = true;
                board[startRow][startCol] = Type.EMPTY;
                board[endRow][endCol] = currType;
                Point mid = findMidSquare(move);

                Type middle = getPiece(mid);
                if (middle == Type.BLACK)
                    numBlackNormalPieces--;
                else if(middle == Type.BLACK_KING){
                    numBlackKingPieces--;

                    //Regicide implementation
                    board[endRow][endCol] = Type.WHITE_KING;
                }
                else if(middle == Type.WHITE)
                    numWhiteNormalPieces--;
                else if(middle == Type.WHITE_KING){
                    numWhiteKingPieces--;

                    //Regicide implementation
                    board[endRow][endCol] = Type.BLACK_KING;
                }
                board[mid.x][mid.y] = Type.EMPTY;
            }

            if (endRow == 0 && side == Player.Side.BLACK) {
                board[endRow][endCol] = Type.BLACK_KING;
                numBlackNormalPieces--;
                numBlackKingPieces++;
            }

            else if (endRow == SIZE - 1 && side == Player.Side.WHITE) {
                board[endRow][endCol] = Type.WHITE_KING;
                numWhiteNormalPieces--;
                numWhiteKingPieces++;
            }
            if (jumpMove) {
                List<Move> additional = getValidSkipMoves(endRow, endCol, side);
                if (additional.isEmpty())
                    return Decision.COMPLETED;
                return Decision.ADDITIONAL_MOVE;
            }
            return Decision.COMPLETED;
        } else
            return Decision.FAILED_INVALID_DESTINATION;
    }

    /**
     * Returns all valid moves
     * @param side the selected side
     * @return all valid moves for the selected side
     */
    public List<Move> getAllValidMoves(Player.Side side)
    {

        Type normal = side == Player.Side.BLACK ? Type.BLACK : Type.WHITE;
        Type king = side == Player.Side.BLACK ? Type.BLACK_KING : Type.WHITE_KING;

        List<Move> possibleMoves = new ArrayList<>();
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                Type t = getPiece(i, j);
                if(t == normal || t == king)
                    possibleMoves.addAll(getValidMoves(i, j, side));
            }
        }


        return possibleMoves;
    }

    /**
     * Find the middle square for a move
     * @param move the concerned move
     * @return the middle square for the provided move
     */
    private Point findMidSquare(Move move) {

        Point ret = new Point((move.getStart().x + move.getEnd().x) / 2,
                (move.getStart().y + move.getEnd().y) / 2);

        return ret;
    }

    /**
     * Checks if the moved Piece is owned by the Player performing the move
     * @param row the row of the piece
     * @param col the col of the piece
     * @param side the Player's side
     * @return if the moved Piece is owned by the Player performing the move or not
     */
    private boolean isMovingOwnPiece(int row, int col, Player.Side side) {
        Type pieceType = getPiece(row, col);
        if (side == Player.Side.BLACK && pieceType != Type.BLACK && pieceType != Type.BLACK_KING)
            return false;
        else if (side == Player.Side.WHITE && pieceType != Type.WHITE && pieceType != Type.WHITE_KING)
            return false;
        return true;
    }

    /**
     * Get all valid moves for a piece
     * @param row the row of the piece
     * @param col the col of the piece
     * @param side piece's side
     * @return all valid moves for the piece at the provided point (row, col)
     */
    public List<Move> getValidMoves(int row, int col, Player.Side side) {
        Type type = board[row][col];
        Point startPoint = new Point(row, col);
        if (type == Type.EMPTY)
            throw new IllegalArgumentException();

        List<Move> moves = new ArrayList<>();

        //If piece is not king
        if (type == Type.WHITE || type == Type.BLACK) {

            int rowChange = type == Type.WHITE ? 1 : -1;

            int newRow = row + rowChange;
            if (newRow >= 0 || newRow < SIZE) {
                int newCol = col + 1;
                if (newCol < SIZE && getPiece(newRow, newCol) == Type.EMPTY)
                    moves.add(new Move(startPoint, new Point(newRow, newCol)));
                newCol = col - 1;
                if (newCol >= 0 && getPiece(newRow, newCol) == Type.EMPTY)
                    moves.add(new Move(startPoint, new Point(newRow, newCol)));
            }

        }
        //If piece is king
        else {
            //4 possible moves

            int newRow = row + 1;
            if (newRow < SIZE) {
                int newCol = col + 1;
                if (newCol < SIZE && getPiece(newRow, newCol) == Type.EMPTY)
                    moves.add(new Move(startPoint, new Point(newRow, newCol)));
                newCol = col - 1;
                if (newCol >= 0 && getPiece(newRow, newCol) == Type.EMPTY)
                    moves.add(new Move(startPoint, new Point(newRow, newCol)));
            }
            newRow = row - 1;
            if (newRow >= 0) {
                int newCol = col + 1;
                if (newCol < SIZE && getPiece(newRow, newCol) == Type.EMPTY)
                    moves.add(new Move(startPoint, new Point(newRow, newCol)));
                newCol = col - 1;
                if (newCol >= 0 && getPiece(newRow, newCol) == Type.EMPTY)
                    moves.add(new Move(startPoint, new Point(newRow, newCol)));
            }


        }

        moves.addAll(getValidSkipMoves(row, col, side));
        return moves;
    }

    /**
     * Get all valid skip moves for a piece
     * @param row the row of the piece
     * @param col the col of the piece
     * @param side piece's side
     * @return all valid skip moves for the piece at the provided point (row, col)
     */
    List<Move> getValidSkipMoves(int row, int col, Player.Side side) {
        List<Move> move = new ArrayList<>();
        Point start = new Point(row, col);

        List<Point> possibilities = new ArrayList<>();

        if(side == Player.Side.WHITE && getPiece(row, col) == Type.WHITE)
        {
            possibilities.add(new Point(row + 2, col + 2));
            possibilities.add(new Point(row + 2, col - 2));
        }
        else if(side == Player.Side.BLACK && getPiece(row, col) == Type.BLACK)
        {
            possibilities.add(new Point(row - 2, col + 2));
            possibilities.add(new Point(row - 2, col - 2));
        }
        else if(getPiece(row, col) == Type.BLACK_KING || getPiece(row, col) == Type.WHITE_KING)
        {
            possibilities.add(new Point(row + 2, col + 2));
            possibilities.add(new Point(row + 2, col - 2));
            possibilities.add(new Point(row - 2, col + 2));
            possibilities.add(new Point(row - 2, col - 2));
        }

        for (Point temp : possibilities) {
            Move m = new Move(start, temp);
            if (temp.x < SIZE && temp.x >= 0 && temp.y < SIZE && temp.y >= 0 && getPiece(temp.x, temp.y) == Type.EMPTY
                    && isOpponentPiece(side, getPiece(findMidSquare(m)))) {
                move.add(m);
            }
        }

        return move;
    }

    /**
     * Checks if piece type is opponent piece type
     * @param current current player side
     * @param opponentPiece opponent piece type
     * @return true if piece type is opponent piece type otherwise false
     */
    private boolean isOpponentPiece(Player.Side current, Type opponentPiece) {
        if (current == Player.Side.BLACK && (opponentPiece == Type.WHITE || opponentPiece == Type.WHITE_KING))
            return true;
        if (current == Player.Side.WHITE && (opponentPiece == Type.BLACK || opponentPiece == Type.BLACK_KING))
            return true;
        return false;
    }

    /**
     * Get all available jump moves for a player
     * @param side player side
     * @return all available jump moves for the player on the provided side
     */
    public List<Move> getJumpMoves(Player.Side side){
        List<Move> jumpMoves = new ArrayList<>();
        for(Move m: getAllValidMoves(side)){
            if(m.getStart().x+1 != m.getEnd().x && m.getStart().x-1 != m.getEnd().x){
                jumpMoves.add(m);
            }
        }
        return  jumpMoves;
    }

    /**
     * Get all available jump moves for a Piece
     * @param x position on x axis
     * @param y position on y axis
     * @param jumpMoves all jumps moves
     * @return all available jump moves for a Piece at (x, y)
     */
    public Boolean jumpMoveAvailableForPiece(int x, int y, List<Move> jumpMoves){
        for(Move m: jumpMoves ){
            if(m.getStart().x==x && m.getStart().y==y){
                return  true;
            }
        }
        return false;
    }


    public Board clone() {
        Type[][] newBoard = new Type[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++)
        {
            System.arraycopy(board[i], 0, newBoard[i], 0, SIZE);
        }
        return new Board(newBoard);
    }
}
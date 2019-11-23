package processing;

/**
 * Player class, also base for PlayerAI class
 */
public class Player {
    private Side side;

    public enum Side {
        BLACK, WHITE
    }

    public Player(Side side) {
        this.side = side;
    }

    public Side getSide()
    {
        return side;
    }

    public Board.Decision makeMove(Move m, Board b)
    {
        return b.makeMove(m, side);
    }
}
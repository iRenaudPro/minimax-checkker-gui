package processing;

/**
 * AI interface implemented by PlayerAI class
 */
public interface AI{
    Board.Decision makeMove(Board b);
}
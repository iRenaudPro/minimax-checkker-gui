package processing;

import java.awt.*;

/**
 * Move class representing moves performed by Players.
 */
public class Move {

    private Point start;
    private Point end;

    public Move(int startRow, int startCol, int endRow, int endCol)
    {
        start = new Point(startRow, startCol);
        end = new Point(endRow, endCol);
    }

    Move(Point start, Point end)
    {
        this.start = start;
        this.end = end;
    }

    public Point getStart()
    {
        return start;
    }

    public Point getEnd()
    {
        return end;
    }

    public boolean equals(Object m)
    {
        if(!(m instanceof Move))
            return false;
        Move x = (Move) m;
        if(this.getStart().equals(x.getStart()) && this.getEnd().equals(x.getEnd()))
            return true;

        return false;
    }

}
package Chess;

public class Point
{
    protected final int row;
    protected final int column;

    public Point(int r, int c)
    {
        this.row = r;
        this.column = c;
    }

    public int getRow() { return row; }

    public int getColumn() { return column; }
}

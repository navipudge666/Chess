package Chess;

public class Move
{
    private final Point from;
    private final Point to;
    private boolean gameIsFinished;

    public Move(Point from, Point to)
    {
        this.from = from;
        this.to = to;
        gameIsFinished = false;
    }

    public Move(Square from, Square to)
    {
        this.from = from.getPoint();
        this.to = to.getPoint();
    }

    public Point getFrom() { return from; }

    public Point getTo() { return to; }

    public boolean isGameIsFinished()
    {
        return gameIsFinished;
    }

    public void setGameIsFinished(boolean gameIsFinished)
    {
        this.gameIsFinished = gameIsFinished;
    }
}

package Chess;

public class Bishop extends Piece
{
    public Bishop(int color, Square square, String imageFile)
    {
        super(color, square, imageFile);
    }

    @Override
    public boolean MoveIsPossible(Point to)
    {
        return false;
    }
}

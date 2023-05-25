package Chess;

public class King extends  Piece
{
    public King(int color, Square square, String imageFile)
    {
        super(color, square, imageFile);
    }

    @Override
    public boolean MoveIsPossible(Point to)
    {
        return false;
    }
}

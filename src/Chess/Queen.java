package Chess;

public class Queen extends Piece
{
    public Queen(int color, Square square, String imageFile)
    {
        super(color, square, imageFile);
    }

    @Override
    public boolean MoveIsPossible(Point to)
    {
        return false;
    }
}

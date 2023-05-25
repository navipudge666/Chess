package Chess;

public class Knight extends Piece
{
    public Knight(int color, Square square, String imageFile)
    {
        super(color, square, imageFile);
    }

    @Override
    public boolean MoveIsPossible(Point to)
    {
        return false;
    }
}

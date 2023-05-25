package Chess;

public class Rook extends Piece
{
    public Rook(int color, Square square, String imageFile)
    {
        super(color, square, imageFile);
    }

    @Override
    public boolean MoveIsPossible(Point to)
    {
        return true;
    }
}

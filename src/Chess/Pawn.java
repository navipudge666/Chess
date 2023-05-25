package Chess;

import java.awt.image.BufferedImage;

public class Pawn extends Piece
{
    private boolean hasMoved = false;
    boolean canEnPassant = true;

    public Pawn(int color, Square square, String imageFile)
    {
        super(color, square, imageFile);
    }


    @Override
    public boolean MoveIsPossible(Point to)
    {
        return true;
    }
}

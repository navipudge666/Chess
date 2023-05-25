package Chess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public abstract class Piece
{
    final int color;
    Square square;
    Image image;

    public Piece(int color, Square square, String imageFile)
    {
        this.color = color;
        this.square = square;
        try
        {
            this.image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(imageFile)));
        }
        catch (IOException e)
        {
            System.out.print(e.getMessage());
        }
    }

    public boolean makeMove(Square destination)
    {
        if (destination == this.square)
            return false;
        destination.setPiece(this);
        square.setPiece(null);
        square = destination;
        return  true;
    }

    public void draw(Graphics g)
    {
        int x = square.getX();
        int y = square.getY();
        int width = square.getWidth();
        int height = square.getHeight();
        g.drawImage(image, x, y, width, height, null);
    }

    public void draw(Graphics g, int x, int y)
    {
        int width = square.getWidth();
        int height = square.getHeight();
        g.drawImage(image, x, y, width, height, null);
    }

    public int getColor() { return color; }

    public Image getImage() { return image; }

    public Square getSquare() { return square; }

    public void setSquare(Square square) { this.square = square; }
    public abstract boolean MoveIsPossible(Point to);

}

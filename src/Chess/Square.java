package Chess;

import Chess.Piece;
import GUI.BoardPanel;

import javax.swing.*;
import java.awt.*;

public class Square extends JComponent
{
    private final BoardPanel boardPanel;
    private final int color;
    private final int row;
    private final int column;
    private final Point point;
    private Piece piece;
    private boolean pieceIsVisible;

    public Square(BoardPanel boardPanel, int color, int row, int column)
    {
        this.boardPanel = boardPanel;
        this.color = color;
        this.row = row;
        this.column = column;
        this.pieceIsVisible = true;
        this.point = new Point(row, column);
    }

    public int getColor() { return color; }

    public int getRow() { return row; }

    public int getColumn() { return column; }

    public Point getPoint() { return point; }

    public void setPiece(Piece piece) { this.piece = piece; }

    public Piece getPiece() { return piece; }

    public void setPieceIsVisible(boolean value) { pieceIsVisible = value; }

    public boolean getPieceIsVisible() { return pieceIsVisible; }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Color color = new Color(221,192,127);
        if (this.color == -1)
            color = new Color(101,67,33);
        g.setColor(color);
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        if (this.piece != null && this.pieceIsVisible)
            piece.draw(g);
    }
}

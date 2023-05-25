package GUI;

import Chess.Board;
import Chess.Move;
import Chess.Piece;
import Chess.Point;
import Chess.Square;
import Network.NetWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class BoardPanel extends JPanel implements MouseListener, MouseMotionListener
{
    private final Board board;
    private Piece currentPiece;
    private int currentX;
    private int currentY;
    private final int playerColor;
    private final String playerUsername;
    private boolean gameIsFinished = false;
    private boolean gameIsStarted = false;
    private final GameFrame gameFrame;

    public BoardPanel(String playerUsername, int playerColor, GameFrame gameFrame, NetWorker netWorker)
    {
        this.board = new Board(netWorker);
        this.playerUsername = playerUsername;
        this.playerColor = playerColor;
        this.gameFrame = gameFrame;
        setLayout(new GridLayout(8,8,0,0));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                int color = (r + c) % 2 == 1 ? -1 : 1;
                Square square = new Square(this, color, r, c);
                board.getSquares()[playerColor == 1 ? 7 - r : r][playerColor == 1 ? c : 7 - c] = square;
                this.add(square);
            }
        }

        board.initializePieces();

        this.setSize(new Dimension(800, 800));

        this.repaint();
        if (this.playerColor == -1)
            gameIsStarted = true;
    }

    public String getPlayerUsername() { return playerUsername; }

    @Override
    public void paintComponent(Graphics g)
    {
        //super.paintComponent(g);
        for (int r = 0; r < 8; r++)
        {
            for (int c = 0; c < 8; c++)
            {
                Square square = this.board.getSquares()[r][c];
                square.paintComponent(g);
            }
        }
        if (currentPiece != null)
        {
            if (currentPiece.getColor() == board.getCurrentTurn())
            {
                currentPiece.draw(g, currentX, currentY);
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!gameIsStarted || board.getGameResult() != 0 || playerColor != board.getCurrentTurn())
            return;
        currentX = e.getX();
        currentY = e.getY();
        Square square = (Square) this.getComponentAt(e.getPoint());
        System.out.println(square.getRow() + " " + square.getColumn());
        currentPiece = square.getPiece();
        if (currentPiece != null)
        {
            System.out.println(currentPiece.getColor() + " " + board.getCurrentTurn());
            if (currentPiece.getColor() != board.getCurrentTurn())
            {
                currentPiece = null;
                return;
            }
            currentX -= currentPiece.getSquare().getWidth() / 2;
            currentY -= currentPiece.getSquare().getHeight() / 2;
            square.setPieceIsVisible(false);
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (!gameIsStarted || board.getGameResult() != 0 || playerColor != board.getCurrentTurn())
            return;
        currentX = e.getX();
        currentY = e.getY();
        if (currentPiece != null)
        {
            currentX -= currentPiece.getSquare().getWidth() / 2;
            currentY -= currentPiece.getSquare().getHeight() / 2;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (!gameIsStarted || board.getGameResult() != 0 || playerColor != board.getCurrentTurn())
            return;
        Square dest = (Square) this.getComponentAt(e.getPoint());
        Move move = null;
        if (currentPiece != null)
        {
            int r1 = currentPiece.getSquare().getRow();
            int c1 = currentPiece.getSquare().getColumn();
            int r2 = dest.getRow();
            int c2 = dest.getColumn();
            if (playerColor == 1) {
                r1 = 7 - r1;
                r2 = 7 - r2;
            } else {
                c1 = 7 - c1;
                c2 = 7 - c2;
            }
            move = new Move(new Point(r1, c1), new Point(r2, c2));
            if (!board.performMove(move))
                move = null;
            gameFrame.updateCurrentTurnLabel();
            //board.performMove(new Move(new Point(r1, c1), new Point(r2, c2)));
            currentPiece.getSquare().setPieceIsVisible(true);
            currentPiece = null;
        }
        gameFrame.updateCurrentTurnLabel();
        repaint();
        if (move != null)
            board.sendMove(playerUsername, move);
    }

    public void getMove()
    {
        gameFrame.updateCurrentTurnLabel();
        board.getMove();
        gameFrame.updateCurrentTurnLabel();
    }

    public Board getBoard() { return board; }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if (!gameIsStarted)
        {
            String name = board.waitForJoining();
            gameFrame.setBlackUsername(name);
            System.out.println("Joined " + name);
            gameIsStarted = true;
        }
        if (board.getGameResult() == 0 && playerColor != board.getCurrentTurn())
            getMove();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }
}

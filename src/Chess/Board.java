package Chess;

import Network.NetWorker;

import java.io.IOException;

public class Board
{
    private static final String whitePawnFile = "white_pawn.png";
    private static final String whiteBishopFile = "white_bishop.png";
    private static final String whiteKingFile = "white_king.png";
    private static final String whiteKnightFile = "white_knight.png";
    private static final String whiteQueenFile = "white_queen.png";
    private static final String whiteRookFile = "white_rook.png";
    private static final String blackBishopFile = "black_bishop.png";
    private static final String blackKingFile = "black_king.png";
    private static final String blackKnightFile = "black_knight.png";
    private static final String blackPawnFile = "black_pawn.png";
    private static final String blackQueenFile = "black_queen.png";
    private static final String blackRookFile = "black_rook.png";

    private final Square[][] squares;
    private int currentTurn = 1;
    private int gameResult;
    private String winner;

    private final NetWorker worker;

    public Board(NetWorker netWorker)
    {
        this.squares = new Square[8][8];
        this.worker = netWorker;
        this.gameResult = 0;
    }

    public Square[][] getSquares() { return squares; }

    public void initializePieces()
    {
        for (int c = 0; c < 8; c++)
        {
            squares[1][c].setPiece(new Pawn(1, squares[1][c], whitePawnFile));
            squares[6][c].setPiece(new Pawn(-1, squares[6][c], blackPawnFile));
        }

        squares[0][0].setPiece(new Rook(1, squares[0][0], whiteRookFile));
        squares[0][7].setPiece(new Rook(1, squares[0][7], whiteRookFile));
        squares[7][0].setPiece(new Rook(-1, squares[7][0], blackRookFile));
        squares[7][7].setPiece(new Rook(-1, squares[7][7], blackRookFile));

        squares[0][1].setPiece(new Knight(1, squares[0][1], whiteKnightFile));
        squares[0][6].setPiece(new Knight(1, squares[0][6], whiteKnightFile));
        squares[7][1].setPiece(new Knight(-1, squares[7][1], blackKnightFile));
        squares[7][6].setPiece(new Knight(-1, squares[7][6], blackKnightFile));

        squares[0][2].setPiece(new Bishop(1, squares[0][2], whiteBishopFile));
        squares[0][5].setPiece(new Bishop(1, squares[0][5], whiteBishopFile));
        squares[7][2].setPiece(new Bishop(-1, squares[7][2], blackBishopFile));
        squares[7][5].setPiece(new Bishop(-1, squares[7][5], blackBishopFile));

        squares[0][3].setPiece(new Queen(1, squares[0][3], whiteQueenFile));
        squares[7][3].setPiece(new Queen(-1, squares[7][3], blackQueenFile));

        squares[0][4].setPiece(new King(1, squares[0][4], whiteKingFile));
        squares[7][4].setPiece(new King(-1, squares[7][4], blackKingFile));
    }

    public int getCurrentTurn() { return currentTurn; }

    public void changeCurrentTurn() { currentTurn *= -1; }

    public boolean performMove(Move move)
    {
        Square from = squares[move.getFrom().getRow()][move.getFrom().getColumn()];
        Square to = squares[move.getTo().getRow()][move.getTo().getColumn()];
        Piece capturedPiece = to.getPiece();
        if (capturedPiece != null && capturedPiece.getClass() == King.class)
        {
            move.setGameIsFinished(true);
            if (capturedPiece.getColor() == 1)
                winner = "Black";
            else
                winner = "White";
            gameResult = capturedPiece.getColor() * (-1);
            return true;
        }
        if (from.getPiece().getColor() != currentTurn)
            return false;
        if (from.getPiece().makeMove(to))
        {
            changeCurrentTurn();
            return true;
        }
        return false;
    }

    public void sendMove(String username, Move move)
    {
        try {
            worker.sendMove(username, move);
            //if (!gameIsFinished)
                //getMove();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getGameResult() { return gameResult; }

    public void setGameResult(int result) { gameResult = result; }

    public void getMove()
    {
        try {
            Move move = worker.getMove();
            if (move.getFrom().getRow() == -1)
                gameResult = currentTurn;
            else
                performMove(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String waitForJoining()
    {
        try {
            return worker.waitForJoining();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }
}

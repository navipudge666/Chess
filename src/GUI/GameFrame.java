package GUI;

import Network.NetWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class GameFrame extends JFrame
{
    public static final int squareSize = 100;
    private final String whiteUsername;
    private String blackUsername;
    private final NetWorker netWorker;

    private final BoardPanel boardPanel;
    private final JLabel blackPlayerLabel = new JLabel("Black: ");
    private final JLabel whitePlayerLabel = new JLabel("White: ");
    private final JLabel currentTurnLabel = new JLabel("Now is White's turn");

    public GameFrame(String whiteUsername, String blackUsername, int playerColor, NetWorker netWorker)
    {
        super("Chess");
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.netWorker = netWorker;

        this.setSize(800,800);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //initializeBoardPanel();

        this.setLayout(new BorderLayout());
        String playerName = playerColor == 1 ? whiteUsername : blackUsername;
        this.boardPanel = new BoardPanel(playerName , playerColor, this, netWorker);
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(initializeInfoPanel(), BorderLayout.EAST);
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                int gameResult = boardPanel.getBoard().getGameResult();
                if (gameResult == 0)
                {
                    if (boardPanel.getPlayerUsername() == whiteUsername)
                        gameResult = -1;
                    else
                        gameResult = 1;
                    boardPanel.getBoard().setGameResult(gameResult);
                }
                onGameFinished(gameResult);
                try {
                    netWorker.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                GameFrame.this.dispose();
            }
        });
    }

    public void setBlackUsername(String username)
    {
        this.blackUsername = username;
        blackPlayerLabel.setText("Black: " + blackUsername);
    }

    public void updateCurrentTurnLabel()
    {
        String currentTurn = "White";
        if (boardPanel.getBoard().getCurrentTurn() == -1)
            currentTurn = "Black";
        currentTurnLabel.setText("Now is " + currentTurn + "'s turn.");
        int gameResult = boardPanel.getBoard().getGameResult();
        if (gameResult == 1)
            currentTurnLabel.setText("White won");
        if (gameResult == -1)
            currentTurnLabel.setText("Black won");
    }

    private JPanel initializeInfoPanel()
    {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JPanel playerNamesPanel = new JPanel();
        playerNamesPanel.setLayout(new BoxLayout(playerNamesPanel, BoxLayout.Y_AXIS));
        playerNamesPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        whitePlayerLabel.setText("White: " + (whiteUsername == null? "" : whiteUsername));
        blackPlayerLabel.setText("Black:" + (blackUsername == null? "" : blackUsername));
        playerNamesPanel.add(whitePlayerLabel);
        playerNamesPanel.add(blackPlayerLabel);
        infoPanel.add(playerNamesPanel);
        infoPanel.add(currentTurnLabel);
        return infoPanel;
    }

    private void onGameFinished(int gameResult)
    {
        try {
            String winner;
            String loser;
            if (gameResult == 1)
            {
                winner = whiteUsername;
                loser = blackUsername;
            }
            else
            {
                winner = blackUsername;
                loser = whiteUsername;
            }
            netWorker.sendGameResult(winner, loser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

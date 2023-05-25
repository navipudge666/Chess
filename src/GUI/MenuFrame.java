package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.Flow;

public class MenuFrame extends JFrame
{
    private final JButton listGamesButton = new JButton("List games");
    private final JButton leaderBoardButton = new JButton("Leaderboard");
    private final JButton exitButton = new JButton("Exit");
    private final JTextField userNameField = new JTextField("Player", 10);
    private final JLabel inputMessage = new JLabel("Your username:");

    public MenuFrame()
    {
        super("Chess");
        setSize(400, 300);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = this.getContentPane();

        setLayout(new BorderLayout());
        initializeButtonPanel();
    }

    private void initializeButtonPanel()
    {
        initializeButtons();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.add(buttonsPanel, BorderLayout.CENTER);

        JPanel enterUserNamePanel = new JPanel();
        enterUserNamePanel.setLayout(new FlowLayout());
        enterUserNamePanel.add(inputMessage);
        enterUserNamePanel.add(userNameField);
        enterUserNamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(enterUserNamePanel);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(listGamesButton);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(leaderBoardButton);
        buttonsPanel.add(Box.createVerticalGlue());
        buttonsPanel.add(exitButton);
    }

    private void initializeButtons()
    {
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> {
                MenuFrame.this.setVisible(false);
                MenuFrame.this.dispose();
        });

        listGamesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        listGamesButton.addActionListener(e -> {
            String playerName = userNameField.getText();
            try {
                new ListGamesFrame(playerName).setVisible(true);
                System.out.println("Connected as " + playerName);
            } catch (Exception ioException) {
                ioException.printStackTrace();
            }
        });

        //leaderBoardButton.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        leaderBoardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderBoardButton.addActionListener(e ->
        {
            try {
                new LeaderBoardFrame().setVisible(true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}

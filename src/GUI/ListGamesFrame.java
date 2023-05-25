package GUI;

import Network.NetWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ListGamesFrame extends JFrame
{
    private final String playerName;

    private final String[] testData = {"Dendi", "Pudge"};
    private final DefaultListModel<String> dlm = new DefaultListModel<String>();
    JList<String> list = new JList<>(testData);
    private final JButton addButton = new JButton("New Game");
    private final JButton connectButton = new JButton("Connect");
    private final JButton refreshButton = new JButton("Refresh");
    private final NetWorker netWorker;

    public static final String host = "127.0.0.1";
    public static final int port = 8081;

    public ListGamesFrame(String playerName) throws Exception
    {
        super("Chess");
        this.setSize(800, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.playerName = playerName;
        this.netWorker = new NetWorker(host, port);
        this.netWorker.setName(playerName);
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                try {
                    netWorker.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                ListGamesFrame.this.dispose();
            }
        });
        initializeButtons();
        initializeContent();
        refreshList();
    }

    private void initializeContent()
    {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel listContent = new JPanel();

        list = new JList<>(dlm);
        list.setSize(640, 480);
        for (String str : testData)
            dlm.add(0, str);
        JScrollPane listPane = new JScrollPane(list);
        list.setVisibleRowCount(20);
        listPane.setPreferredSize(new Dimension(700, 450));
        //listPane.setPreferredSize(list.getPreferredSize());
        listContent.add(listPane);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        buttonsPanel.add(addButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(refreshButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(connectButton);

        content.add(listContent, BorderLayout.CENTER);
        content.add(buttonsPanel, BorderLayout.SOUTH);
        this.add(content);
    }

    private void initializeButtons()
    {
        addButton.addActionListener(e -> {
            try {
                netWorker.createGame(playerName);
                GameFrame gameFrame = new GameFrame(playerName, null, 1, netWorker);
                gameFrame.setVisible(true);
                this.dispose();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            dlm.add(dlm.getSize(), this.playerName);
            ListGamesFrame.this.validate();
        });

        connectButton.addActionListener(e -> {
            int selectedIndex = list.getSelectedIndex();
            if (list.getSelectedIndex() == -1)
            {
                JOptionPane.showMessageDialog(null, "Select game before connecting",
                        "Error", JOptionPane.PLAIN_MESSAGE);
            }
            else
            {
                String username = list.getSelectedValue();
                try {
                    netWorker.connect(username, playerName);
                    GameFrame gameFrame = new GameFrame(username, playerName, -1, netWorker);
                    gameFrame.setVisible(true);
                    this.dispose();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Can't connect to this game",
                            "Error", JOptionPane.PLAIN_MESSAGE);
                }
                dlm.remove(list.getSelectedIndex());
            }
        });

        refreshButton.addActionListener(e ->
        {
            refreshList();
        });
    }

    private void refreshList()
    {
        try {
            String[] games = netWorker.getGames();
            dlm.clear();
            for (String game : games)
                dlm.add(dlm.getSize(), game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package GUI;

import Network.NetWorker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class LeaderBoardFrame extends JFrame
{
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Player", "Wins", "Loses"}, 0);
    private final JButton refreshButton = new JButton("Refresh");
    private final NetWorker netWorker;

    public static final String host = "127.0.0.1";
    public static final int port = 8081;

    public LeaderBoardFrame() throws IOException
    {
        super("Chess");
        this.setSize(800, 600);
        this.setResizable(false);
        this.netWorker = new NetWorker(host, port);

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
                LeaderBoardFrame.this.dispose();
            }
        });

        initializeButtons();
        initializeContent();
    }

    private void initializeButtons()
    {
        refreshButton.addActionListener(e ->
        {
            refresh();
        });
    }

    private void initializeContent()
    {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        //model.addRow(new Object[]{"Dendi", 100, 0});
        //model.addRow(new Object[]{"Pudge", 50, 50});
        //model.addRow(new Object[]{"Noob", 0, 100});
        JTable table = new JTable(model);
        table.setDragEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sorter.setComparator(1, new DoubleComparator());
        sorter.setComparator(2, new DoubleComparator());
        table.setRowSorter(sorter);
        table.setSize(640, 480);
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 10));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        buttonsPanel.add(refreshButton);
        //buttonsPanel.add(Box.createHorizontalGlue());


        content.add(new JScrollPane(table), BorderLayout.CENTER);
        content.add(buttonsPanel, BorderLayout.SOUTH);

        this.add(content);
        refresh();
    }

    static class DoubleComparator implements Comparator<Double>
    {
        @Override
        public int compare(Double o1, Double o2)
        {
            return Double.compare(o1, o2);
        }
    }

    private void refresh()
    {
        try {
            String[] answer = netWorker.getStats();
            while (model.getRowCount() > 0)
                model.removeRow(model.getRowCount() - 1);
            for (int i = 0; i < answer.length; i+=3)
            {
                model.addRow(new Object[]{ answer[i], Double.parseDouble(answer[i+1]), Double.parseDouble(answer[i+2])});
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}

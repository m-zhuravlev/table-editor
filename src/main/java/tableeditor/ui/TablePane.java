package tableeditor.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;

public class TablePane extends JPanel {
    private final JTextField field;
    private JTable table;


    public TablePane() {
        super();
        setLayout(new BorderLayout());
        field = new JTextField();
        field.setEditable(false);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(Box.createVerticalStrut(4));
        topPanel.add(createTopContent());
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(2));
        centerPanel.add(createCenterContent());
        centerPanel.add(Box.createVerticalStrut(2));
        add(centerPanel, BorderLayout.CENTER);
    }

    private Component createTopContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalStrut(MyTable.ZERO_COLUMN_SIZE));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        return panel;
    }

    private Component createCenterContent() {
        table = new MyTable(field);
        table.getColumnModel().getSelectionModel().addListSelectionListener(this::selectionChangeHandler);
        table.getSelectionModel().addListSelectionListener((this::selectionChangeHandler));
        return new JScrollPane(table);
    }


    private void selectionChangeHandler(ListSelectionEvent e) {
        int col = table.getSelectedColumn();
        String text = "";
        if (col > 0) {
            Object val = table.getValueAt(table.getSelectedRow(), col);
            if (val != null) {
                text = val.toString();
            }
        }
        field.setText(text);
    }

}

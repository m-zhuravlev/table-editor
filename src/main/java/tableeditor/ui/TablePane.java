package tableeditor.ui;

import javax.swing.*;
import java.awt.*;

public class TablePane extends JPanel {

    public TablePane() {
        super();
        setLayout(new BorderLayout());
        JTextField field = new JTextField();
        field.setEditable(true);
        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(Box.createVerticalStrut(4));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalStrut(MyTable.ZERO_COLUMN_SIZE));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        topPanel.add(panel);
        add(topPanel, BorderLayout.NORTH);
        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(2));
        centerPanel.add(new JScrollPane(new MyTable(field)));
        centerPanel.add(Box.createVerticalStrut(2));
        add(centerPanel, BorderLayout.CENTER);
    }


}

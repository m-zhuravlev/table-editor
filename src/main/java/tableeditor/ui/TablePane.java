package tableeditor.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TablePane extends JPanel {

    public TablePane() {
        super();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(1, 1, 1, 1));
        // Top panel
        JTextField field = new JTextField();
        field.setEditable(true);
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

        // Bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createRigidArea(new Dimension(MyTable.ZERO_COLUMN_SIZE, 14)));
        JLabel lbl = new JLabel();
        lbl.setFont(new Font(lbl.getFont().getName(),Font.BOLD, 10));
        bottomPanel.add(lbl);
        add(bottomPanel, BorderLayout.SOUTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(2));
        centerPanel.add(new JScrollPane(new MyTable(field, lbl)));
        add(centerPanel, BorderLayout.CENTER);

    }


}

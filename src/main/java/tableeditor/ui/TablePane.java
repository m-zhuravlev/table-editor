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
        Box topPanel = Box.createVerticalBox();
        topPanel.add(Box.createVerticalStrut(4));

        Box hPanel = Box.createHorizontalBox();
        hPanel.add(Box.createHorizontalStrut(MyTable.ZERO_COLUMN_SIZE));

        JPanel bPanel = new JPanel(new BorderLayout());
        JTextField selectionField = new JTextField();
        selectionField.setEditable(false);
        selectionField.setPreferredSize(new Dimension(80, selectionField.getHeight()));
        bPanel.add(selectionField, BorderLayout.WEST);

        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(2));
        JTextField expressionField = new JTextField();
        expressionField.setEditable(true);
        hBox.add(expressionField);
        bPanel.add(hBox, BorderLayout.CENTER);

        hPanel.add(bPanel);
        hPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        topPanel.add(hPanel);
        add(topPanel, BorderLayout.NORTH);

        // Bottom
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createRigidArea(new Dimension(MyTable.ZERO_COLUMN_SIZE, 14)));
        JLabel lbl = new JLabel();
        lbl.setFont(new Font(lbl.getFont().getName(), Font.BOLD, 10));
        bottomPanel.add(lbl);
        add(bottomPanel, BorderLayout.SOUTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalStrut(2));
        centerPanel.add(new JScrollPane(new MyTable(selectionField,expressionField, lbl)));
        add(centerPanel, BorderLayout.CENTER);

    }


}

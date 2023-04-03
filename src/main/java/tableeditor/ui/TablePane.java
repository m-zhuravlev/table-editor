package tableeditor.ui;

import tableeditor.expression.enums.FunctionEnum;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class TablePane extends JPanel {

    public TablePane() {
        super();
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(1, 1, 1, 1));

        // Top panel
        JTextField selectionField = new JTextField();
        JTextField expressionField = new JTextField();
        Box topPanel = createTop(selectionField, expressionField);
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
        centerPanel.add(new JScrollPane(new MyTable(selectionField, expressionField, lbl)));
        add(centerPanel, BorderLayout.CENTER);

    }

    private static Box createTop(JTextField selectionField, JTextField expressionField) {
        Box topPanel = Box.createVerticalBox();
        topPanel.add(Box.createVerticalStrut(4));

        Box hPanel = Box.createHorizontalBox();
        hPanel.add(Box.createHorizontalStrut(MyTable.ZERO_COLUMN_SIZE));

        JPanel bPanel = new JPanel(new BorderLayout());
        selectionField.setEditable(false);
        selectionField.setPreferredSize(new Dimension(80, selectionField.getHeight()));
        bPanel.add(selectionField, BorderLayout.WEST);

        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(2));
        JLabel fxLbl = new JLabel("F(x)");
        fxLbl.setFont(new Font(fxLbl.getFont().getName(), Font.BOLD, 12));
        hBox.add(fxLbl);

        JPopupMenu menu = new JPopupMenu();
        for (FunctionEnum item : FunctionEnum.values()) {
            menu.add(item.functionName).addActionListener((event) -> {
                String text = expressionField.getText();
                if (text.startsWith("=")) {
                    text += " " + item.functionName + "(";
                } else {
                    text = "=" + item.functionName + "(";
                }
                expressionField.setText(text);
            });
        }
        fxLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menu.show(fxLbl, fxLbl.getX(), fxLbl.getY() + fxLbl.getHeight());
            }
        });
        hBox.add(Box.createHorizontalStrut(2));
        expressionField.setEditable(true);
        hBox.add(expressionField);
        bPanel.add(hBox, BorderLayout.CENTER);

        hPanel.add(bPanel);
        hPanel.add(Box.createRigidArea(new Dimension(2, 0)));

        topPanel.add(hPanel);
        return topPanel;
    }


}

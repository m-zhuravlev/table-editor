package tableeditor.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.awt.*;
import java.util.stream.IntStream;

public class TablePane extends JPanel {

    public static final int ZERO_COLUMN_SIZE = 32;
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
        panel.add(Box.createHorizontalStrut(ZERO_COLUMN_SIZE));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        return panel;
    }

    private Component createCenterContent() {
        TableModel tableModel = new MyTableModel();

        DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
        DefaultTableCellRenderer zeroRenderer = new DefaultTableCellRenderer();
        zeroRenderer.setFocusable(false);
        zeroRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumn zeroColumn = new TableColumn(0);
        zeroColumn.setCellRenderer(zeroRenderer);
        zeroColumn.setPreferredWidth(ZERO_COLUMN_SIZE);
        columnModel.addColumn(zeroColumn);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setVerticalAlignment(JLabel.CENTER);
        cellRenderer.setHorizontalAlignment(JLabel.LEFT);

        IntStream.range(1, tableModel.getColumnCount()).forEach(i -> {
            TableColumn column = new TableColumn(i);
            column.setPreferredWidth(80);
            column.setCellRenderer(cellRenderer);
            column.setHeaderValue(tableModel.getColumnName(i));
            columnModel.addColumn(column);
        });

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table = new JTable(tableModel, columnModel, selectionModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(20);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setCellSelectionEnabled(true);
        table.setDefaultEditor(MyTableModel.CellModel.class, new CellModelEditor());

        table.setColumnSelectionInterval(1, 1);
        table.setRowSelectionInterval(0, 0);

        zeroRenderer.setBackground(table.getTableHeader().getBackground());

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

    public static class MyTableModel extends AbstractTableModel {
        public static final int ROW_COUNT = 52;
        public static final int COLUMN_COUNT = 27;

        CellModel[][] data = new CellModel[ROW_COUNT][COLUMN_COUNT];

        @Override
        public int getRowCount() {
            return ROW_COUNT;
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Integer.class : CellModel.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex > 0;
        }

        @Override
        public String getColumnName(int column) {
            return ColumnUtils.numberToName(column);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return columnIndex == 0 ? Integer.valueOf(rowIndex + 1) : data[rowIndex][columnIndex - 1];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex > 0) {
                if (data[rowIndex][columnIndex - 1] == null) {
                    data[rowIndex][columnIndex - 1] = new CellModel();
                }
                data[rowIndex][columnIndex - 1].setText(String.valueOf(aValue));
            }
        }

        private class CellModel {
            private String text = "";

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            @Override
            public String toString() {
                return text;
            }
        }
    }

    private class CellModelEditor extends DefaultCellEditor {

        public CellModelEditor() {
            super(new JTextField());
            JTextField tf = (JTextField) editorComponent;
            tf.setBorder(new EmptyBorder(0, 0, 0, 0));
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JTextField tf = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            if (value instanceof MyTableModel.CellModel) {
                tf.setText(((MyTableModel.CellModel) value).getText());
            }
            tf.selectAll();
            return tf;
        }
    }
}

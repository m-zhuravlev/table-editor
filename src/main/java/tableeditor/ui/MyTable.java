package tableeditor.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.stream.IntStream;

public class MyTable extends JTable {

    public static final int ZERO_COLUMN_SIZE = 32;

    private final JTextField topField;

    public MyTable(JTextField field) {
        this.topField = field;
        MyTableModel tableModel = new MyTableModel();

        DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
        DefaultTableCellRenderer zeroRenderer = new DefaultTableCellRenderer();
        zeroRenderer.setFocusable(false);
        zeroRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumn zeroColumn = new TableColumn(0);
        zeroColumn.setCellRenderer(zeroRenderer);
        zeroColumn.setPreferredWidth(ZERO_COLUMN_SIZE);
        columnModel.addColumn(zeroColumn);

        MyCellRenderer cellRenderer = new MyCellRenderer();
        IntStream.range(1, tableModel.getColumnCount()).forEach(i -> {
            TableColumn column = new TableColumn(i);
            column.setPreferredWidth(80);
            column.setCellRenderer(cellRenderer);
            column.setHeaderValue(tableModel.getColumnName(i));
            columnModel.addColumn(column);
        });

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setModel(tableModel);
        this.setColumnModel(columnModel);
        this.setSelectionModel(selectionModel);

        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowHeight(20);
        this.setGridColor(Color.LIGHT_GRAY);
        this.setCellSelectionEnabled(true);
        this.setDefaultEditor(CellModel.class, new CellModelEditor());

        this.setColumnSelectionInterval(1, 1);
        this.setRowSelectionInterval(0, 0);

        zeroRenderer.setBackground(this.getTableHeader().getBackground());

        columnModel.getSelectionModel().addListSelectionListener(this::selectionChangeHandler);
        selectionModel.addListSelectionListener((this::selectionChangeHandler));

    }

    private void selectionChangeHandler(ListSelectionEvent e) {
        int col = this.getSelectedColumn();
        String text = "";
        if (col > 0) {
            Object val = this.getValueAt(this.getSelectedRow(), col);
            if (val != null) {
                text = val.toString();
            }
        }
        topField.setText(text);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
    }

    private static class MyCellRenderer extends DefaultTableCellRenderer {

        public MyCellRenderer() {
            this.setVerticalAlignment(JLabel.CENTER);
            this.setHorizontalAlignment(JLabel.LEFT);
        }

        protected void setValue(Object value) {
            if (value instanceof CellModel cellModel && !cellModel.getCalculatedValue().isEmpty()) {
                setText(cellModel.getCalculatedValue());
            } else {
                super.setValue(value);
            }
        }
    }

    private class CellModelEditor extends DefaultCellEditor {

        public CellModelEditor() {
            super(new JTextField());
            JTextField tf = (JTextField) editorComponent;
            tf.setBorder(new EmptyBorder(0, 0, 0, 0));
            topField.setDocument(tf.getDocument());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JTextField tf = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
            if (value instanceof CellModel cellModel) {
                tf.setText(cellModel.getText());
            }
            tf.selectAll();
            return tf;
        }

    }
}

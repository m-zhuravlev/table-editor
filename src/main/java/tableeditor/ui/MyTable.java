package tableeditor.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

public class MyTable extends JTable {

    public static final int ZERO_COLUMN_SIZE = 32;
    public static final int COLUMN_PREFERRED_WIDTH = 80;
    public static final int ROW_HEIGHT = 20;

    private final JTextField selectionField;
    private final JTextField exprField;
    private final JLabel errorLabel;
    private final CellModelEditor cellEditor;

    public MyTable(JTextField selectionField, JTextField exprField, JLabel errorLabel) {
        this.selectionField = selectionField;
        this.exprField = exprField;
        this.errorLabel = errorLabel;
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
            column.setPreferredWidth(COLUMN_PREFERRED_WIDTH);
            column.setCellRenderer(cellRenderer);
            column.setHeaderValue(tableModel.getColumnName(i));
            columnModel.addColumn(column);
        });
        columnModel.getSelectionModel().addListSelectionListener(this::selectionChangeHandler);

        DefaultListSelectionModel selectionModel = new DefaultListSelectionModel();
        selectionModel.addListSelectionListener(this::selectionChangeHandler);
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.setModel(tableModel);
        this.setColumnModel(columnModel);
        this.setSelectionModel(selectionModel);

        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowHeight(ROW_HEIGHT);
        this.setGridColor(Color.LIGHT_GRAY);
        this.setCellSelectionEnabled(true);
        cellEditor = new CellModelEditor();
        this.setDefaultEditor(CellModel.class, cellEditor);

        this.setColumnSelectionInterval(1, 1);
        this.setRowSelectionInterval(0, 0);

        zeroRenderer.setBackground(this.getTableHeader().getBackground());


        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = MyTable.this.rowAtPoint(evt.getPoint());
                int col = MyTable.this.columnAtPoint(evt.getPoint());
                int sRow = MyTable.this.getSelectedRow();
                int sCol = MyTable.this.getSelectedColumn();
                if (row >= 0 && col > 0 && (row != sRow || col != sCol)) {
                    Object value = MyTable.this.getValueAt(sRow, sCol);
                    if (value instanceof CellModel cellModel) {
                        if (cellModel.getText().startsWith("=")) {
                            cellModel.calcExpression();
                        }
                    }
                }
            }
        });

        this.exprField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!MyTable.this.isEditing()) {
                    fieldEditingFinished();
                }
            }
        });
        this.exprField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    fieldEditingFinished();
                }
            }
        });

    }

    private void fieldEditingFinished() {
        if (MyTable.this.isEditing()) {
            cellEditor.fireEditingStopped();
        } else {
            Object value = MyTable.this.getValueAt(getSelectedRow(), getSelectedColumn());
            if (value instanceof CellModel cellModel) {
                cellModel.calcExpression();
            }
        }
    }

    private void selectionChangeHandler(ListSelectionEvent e) {
        String text = "";
        int col = getSelectedColumn();
        int row = getSelectedRow();
        if (col >= 0 && row >= 0) {
            if (col == 0) {
                this.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
            } else {
                Object val = getValueAt(row, col);
                if (val instanceof CellModel cellModel) {
                    text = cellModel.getText();
                    errorLabel.setText(cellModel.getErrorMessage());
                }
                selectionField.setText(MyTableModel.numberToName(col) + (row + 1));
            }
        } else {
            selectionField.setText("");
        }
        exprField.setText(text);
    }

    public void editingStopped(ChangeEvent e) {
        // Take in the new value
        TableCellEditor editor = getCellEditor();
        if (editor != null) {
            Object value = editor.getCellEditorValue();
            setValueAt(value, editingRow, editingColumn);
            Object cell = getValueAt(editingRow, editingColumn);
            if (cell instanceof CellModel cellModel) {
                cellModel.calcExpression();
            }
            removeEditor();
        }
    }

    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        if (e.getType() == TableModelEvent.UPDATE) {
            if (getSelectedRow() >= 0 && getSelectedColumn() > 0 &&
                    getSelectedRow() == e.getFirstRow() && getSelectedColumn() == e.getColumn()) {
                Object value = getValueAt(e.getFirstRow(), e.getColumn());
                if (value instanceof CellModel cellModel) {
                    errorLabel.setText(cellModel.getErrorMessage());
                }
            }
        }
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
            exprField.setDocument(tf.getDocument());
            exprField.getDocument().addDocumentListener(new MyDocumentListener());
        }

        @Override
        public void fireEditingStopped() {
            super.fireEditingStopped();
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

    private class MyDocumentListener implements DocumentListener {

        private void documentChangeListener(DocumentEvent e) {
            try {
                int row = MyTable.this.getSelectedRow();
                int col = MyTable.this.getSelectedColumn();
                MyTable.this.setValueAt(e.getDocument().getText(0, e.getDocument().getLength()), row, col);
                ((MyTableModel) MyTable.this.getModel()).fireTableCellUpdated(row, col);
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            documentChangeListener(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            documentChangeListener(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            documentChangeListener(e);
        }
    }
}

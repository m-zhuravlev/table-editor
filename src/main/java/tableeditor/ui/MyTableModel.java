package tableeditor.ui;

import tableeditor.expression.exception.ExpressionException;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
    public static final int ROW_COUNT = 100;
    public static final int COLUMN_COUNT = 100;

    private final CellModel[][] data = new CellModel[ROW_COUNT][COLUMN_COUNT];

    public static int nameToNumber(String name) {
        int result = 0;
        for (int i = 0; i < name.length(); i++) {
            result += (name.charAt(i) - 'A' + 1) * (int) Math.pow(26, name.length() - i - 1);
        }
        return result;
    }

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
        return numberToName(column);
    }

    public static String numberToName(int column) {
        StringBuilder result = new StringBuilder();
        column--;
        for (; column >= 0; column = column / 26 - 1) {
            result.insert(0, (char) ((char) (column % 26) + 'A'));
        }
        return result.toString();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return columnIndex == 0 ? Integer.valueOf(rowIndex + 1) : data[rowIndex][columnIndex - 1];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex > 0) {
            int col = columnIndex - 1;
            if (data[rowIndex][col] == null) {
                data[rowIndex][col] = new CellModel(this, rowIndex, col);
            }
            String text = String.valueOf(aValue);
            CellModel cellModel = data[rowIndex][col];
            cellModel.setText(text);
        }
    }


    public CellModel getOrCreateValueAt(String columnName, int rowIndex) throws ExpressionException {
        int columnIndex = nameToNumber(columnName);
        if (rowIndex < 1 || rowIndex > ROW_COUNT || columnIndex < 1 || columnIndex >= COLUMN_COUNT) {
            throw new ExpressionException("15: Cell link '" + columnName + rowIndex + "' out of table bound");
        }
        Object cur = getValueAt(rowIndex - 1, columnIndex);
        if (cur instanceof CellModel) return (CellModel) cur;
        setValueAt("", rowIndex - 1, columnIndex);
        return (CellModel) getValueAt(rowIndex - 1, columnIndex);
    }
}

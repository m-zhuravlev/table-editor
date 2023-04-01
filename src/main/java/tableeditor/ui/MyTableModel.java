package tableeditor.ui;

import tableeditor.expression.Interpreter;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;

public class MyTableModel extends AbstractTableModel {
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
        return numberToName(column);
    }

    private String numberToName(int column) {
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
            if (data[rowIndex][columnIndex - 1] == null) {
                data[rowIndex][columnIndex - 1] = new CellModel();
            }
            String text = String.valueOf(aValue);
            CellModel cellModel = data[rowIndex][columnIndex - 1];
            cellModel.setText(text);
            if (text.startsWith("=") && text.length() > 1) {
                BigDecimal result = Interpreter.getResult(text.substring(1));
                cellModel.setCalculatedValue(result.toString());
            }
        }
    }

    public Object getValueAt(String columnName, int rowIndex) {
        int columnIndex = nameToNumber(columnName);
        return getValueAt(rowIndex - 1, columnIndex);
    }

    public int nameToNumber(String name) {
        return (name.chars().sum() - 'A' + 1) * name.length();
    }

    public static class CellModel {
        private String text = "";
        private String calculatedValue = text;

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

        public String getCalculatedValue() {
            return calculatedValue;
        }


        public void setCalculatedValue(String calculatedValue) {
            this.calculatedValue = calculatedValue;
        }
    }
}

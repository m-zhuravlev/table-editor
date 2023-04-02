package tableeditor.ui;

import tableeditor.expression.Interpreter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
    public static final int ROW_COUNT = 52;
    public static final int COLUMN_COUNT = 27;

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
            int col = columnIndex - 1;
            if (data[rowIndex][col] == null) {
                data[rowIndex][col] = new CellModel(this, rowIndex, col);
            }
            String text = String.valueOf(aValue);
            CellModel cellModel = data[rowIndex][col];
            if (text.startsWith("=") && text.length() > 1) {
                new CalculateWorker(text, cellModel).execute();
            } else {
                cellModel.setCalculatedValue("");
                cellModel.setText(text);
                cellModel.fireCellModelChange();
            }
        }
    }

    public Object getValueAt(String columnName, int rowIndex) {
        int columnIndex = nameToNumber(columnName);
        return getValueAt(rowIndex - 1, columnIndex);
    }

    public static class CalculateWorker extends SwingWorker<Object, Object> {

        private final String text;
        private final CellModel cellModel;

        public CalculateWorker(String text, CellModel cellModel) {
            this.text = text;
            this.cellModel = cellModel;
        }

        @Override
        protected Object doInBackground() {
            String result;
            try {
                result = new Interpreter(cellModel).getResult(text.substring(1)).toString();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                result = "#ERROR";
            }
            cellModel.setCalculatedValue(result);
            cellModel.setText(text);
            cellModel.fireCellModelChange();
            return result;
        }

        @Override
        protected void done() {
            cellModel.getTableModel().fireTableCellUpdated(cellModel.getRow(), cellModel.getCol() + 1);
        }
    }
}

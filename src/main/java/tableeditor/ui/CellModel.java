package tableeditor.ui;

import java.util.HashSet;
import java.util.Set;

public class CellModel implements CellModelListener {
    private final MyTableModel tableModel;
    private final int row;
    private final int col;
    private String text = "";
    private String calculatedValue = text;
    private Set<CellModelListener> listenerList = null;

    public CellModel(MyTableModel tableModel, int row, int col) {
        this.tableModel = tableModel;
        this.row = row;
        this.col = col;
    }

    public MyTableModel getTableModel() {
        return tableModel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void fireCellModelChange() {
        if (listenerList != null) listenerList.forEach(CellModelListener::linkedCellChanged);
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

    public void addListener(CellModelListener listener) {
        if (listenerList == null) {
            listenerList = new HashSet<>();
        }
        listenerList.add(listener);
    }

    @Override
    public void linkedCellChanged() {
        new MyTableModel.CalculateWorker(text, this).execute();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

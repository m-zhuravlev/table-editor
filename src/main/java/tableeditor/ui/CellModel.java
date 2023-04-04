package tableeditor.ui;

import tableeditor.expression.Interpreter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class CellModel implements CellModelListener {
    private final MyTableModel tableModel;
    private final int row;
    private final int col;
    private String text = "";
    private String calculatedValue = "";
    private Set<CellModelListener> listeners = null;
    private String errorMessage = "";
    private Set<CellModelListener> subscriptions = null;

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
        if (listeners != null) listeners.forEach(CellModelListener::linkedCellChanged);
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

    @Override
    public void addListener(CellModelListener listener) {
        if (listeners == null) {
            listeners = new HashSet<>();
        }
        listeners.add(listener);
    }

    @Override
    public void linkedCellChanged() {
        calcExpression();
    }

    @Override
    public void addSubscription(CellModelListener listener) {
        if (subscriptions == null) {
            subscriptions = new HashSet<>();
        }
        subscriptions.add(listener);
    }

    @Override
    public void removeListener(CellModelListener listener) {
        listeners.remove(listener);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void calcExpression() {
        if (text.startsWith("=") && text.length() > 1) {
            new CalculateWorker().execute();
        } else {
            clearSubscriptions();
            setCalculatedValue("");
            setErrorMessage("");
            fireCellModelChange();
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private void setErrorMessage(String message) {
        errorMessage = message;
    }

    public void clearSubscriptions() {
        if (subscriptions != null) {
            subscriptions.forEach(cellModel -> cellModel.removeListener(this));
            subscriptions.clear();
        }
    }

    public Set<CellModelListener> getSubscriptions() {
        return subscriptions;
    }

    public class CalculateWorker extends SwingWorker<Object, Object> {
        @Override
        protected Object doInBackground() {
            String result;
            CellModel cellModel = CellModel.this;
            try {
                cellModel.clearSubscriptions();
                result = new Interpreter(cellModel).getResult(cellModel.getText().substring(1)).toString();
                cellModel.setErrorMessage("");
            } catch (Exception e) {
                System.err.println(e.getMessage());
                cellModel.setErrorMessage(e.getMessage());
                result = "#ERROR";
            }
            cellModel.setCalculatedValue(result);
            cellModel.fireCellModelChange();
            return result;
        }

        @Override
        protected void done() {
            CellModel.this.getTableModel().fireTableCellUpdated(CellModel.this.getRow(), CellModel.this.getCol() + 1);
        }
    }
}

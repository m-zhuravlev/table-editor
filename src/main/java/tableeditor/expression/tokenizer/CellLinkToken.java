package tableeditor.expression.tokenizer;

import tableeditor.expression.exception.ExpressionException;
import tableeditor.ui.CellModel;
import tableeditor.ui.CellModelListener;
import tableeditor.ui.MyTableModel;

import java.math.BigDecimal;

public class CellLinkToken implements Token, TerminalToken {

    private final String columnName;
    private final String rowName;

    public CellLinkToken(String columnName, String rowName) {
        this.columnName = columnName.toUpperCase();
        this.rowName = rowName;
    }

    @Override
    public String getValue() {
        return columnName + rowName;
    }

    public BigDecimal resolveValue(CellModel cellModel) throws ExpressionException {
        MyTableModel tableModel = cellModel.getTableModel();
        CellModel linkModel = tableModel.getOrCreateValueAt(columnName, Integer.parseInt(rowName));
        if (linkModel == cellModel) throw new ExpressionException("Error: Reflexive link");
        if (isContainsIterativeLink(cellModel, linkModel)) throw new ExpressionException("Error: Iterative link");
        linkModel.addListener(cellModel);
        cellModel.addSubscription(linkModel);
        String value = linkModel.getCalculatedValue();
        if (value.trim().isEmpty()) {
            value = linkModel.getText();
        }
        try {
            return value.trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            throw new ExpressionException("Error: cast to Number value from cell " + columnName + rowName);
        }
    }

    public boolean isContainsIterativeLink(CellModelListener cur, CellModelListener link) {
        if (link.getSubscriptions() != null) {
            if (link.getSubscriptions().contains(cur)) return true;
            if (link.getSubscriptions() != null) {
                for (CellModelListener l : link.getSubscriptions()) {
                    if (isContainsIterativeLink(cur, l)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

package tableeditor.expression.tokenizer;

import tableeditor.expression.exception.ExpressionException;
import tableeditor.ui.CellModel;
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
        Object cellValue = tableModel.getValueAt(columnName, Integer.parseInt(rowName));
        CellModel linkModel;
        if (cellValue instanceof CellModel) {
            linkModel = (CellModel) cellValue;
        } else {
            tableModel.setValueAt("", Integer.parseInt(rowName) - 1, MyTableModel.nameToNumber(columnName));
            linkModel = (CellModel) tableModel.getValueAt(columnName, Integer.parseInt(rowName));
        }
        if (linkModel == cellModel) throw new ExpressionException("Reflexive link not supported");
        linkModel.addListener(cellModel);
        String value = linkModel.getCalculatedValue();
        if (value.trim().isEmpty()) {
            value = linkModel.getText();
        }
        return value.trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(value.trim());
    }
}

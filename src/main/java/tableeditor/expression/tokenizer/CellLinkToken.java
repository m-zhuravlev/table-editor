package tableeditor.expression.tokenizer;

import tableeditor.ui.MyTableModel;

import java.math.BigDecimal;

public class CellLinkToken implements Token, TerminalToken {

    private final String columnName;
    private final String rowName;

    public CellLinkToken(String columnName, String rowName) {
        this.columnName = columnName;
        this.rowName = rowName;
    }

    @Override
    public String getValue() {
        return columnName + rowName;
    }

    public BigDecimal resolveValue(MyTableModel tableModel) {
        Object cellValue = tableModel.getValueAt(columnName, Integer.parseInt(rowName));
        if (cellValue instanceof MyTableModel.CellModel cellmodel) {
            String value = cellmodel.getCalculatedValue();
            return value.trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(value.trim());
        }
        return BigDecimal.ZERO;
    }
}

package tableeditor.expression.tokenizer;

public class CellLinkToken implements Token {

    private String columnName;
    private String rowName;

    public CellLinkToken(String columnName, String rowName) {

        this.columnName = columnName;
        this.rowName = rowName;
    }

    @Override
    public String getValue() {
        return columnName + rowName;
    }
}

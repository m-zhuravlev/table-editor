package tableeditor.expression.tokenizer;

public class OperatorToken implements Token{
    private String value;

    public OperatorToken(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

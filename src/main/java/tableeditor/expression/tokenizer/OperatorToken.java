package tableeditor.expression.tokenizer;

public class OperatorToken implements Token {
    private final String value;

    public OperatorToken(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public int getPriority() {
        if (value.equals("+") || value.equals("-")) return 1;
        return 2;
    }
}

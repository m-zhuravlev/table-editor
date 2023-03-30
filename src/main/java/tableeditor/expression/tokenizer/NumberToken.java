package tableeditor.expression.tokenizer;

public class NumberToken implements Token, TerminalToken {
    private String value;

    public NumberToken(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

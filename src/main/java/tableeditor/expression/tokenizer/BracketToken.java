package tableeditor.expression.tokenizer;

public class BracketToken implements Token {
    private final String value;

    public BracketToken(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

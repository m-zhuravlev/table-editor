package tableeditor.expression.tokenizer;

public class BracketOpenToken implements Token {

    @Override
    public String getValue() {
        return "(";
    }
}

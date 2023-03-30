package tableeditor.expression.tokenizer;

public class NamedFunctionToken implements Token, TerminalToken {
    private String name;

    public NamedFunctionToken(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return name;
    }
}

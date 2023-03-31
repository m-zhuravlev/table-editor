package tableeditor.expression.tokenizer;

public class OperatorToken implements Token {
    private final OperationEnum instance;

    public OperatorToken(OperationEnum instance) {
        this.instance = instance;
    }

    @Override
    public String getValue() {
        return String.valueOf(instance.charValue);
    }

    public int getPriority() {
        return instance.priority;
    }
}

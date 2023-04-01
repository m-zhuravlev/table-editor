package tableeditor.expression.tokenizer;

import java.math.BigDecimal;

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

    public OperationEnum getInstance() {
        return instance;
    }

    public BigDecimal execute(BigDecimal a, BigDecimal b) {
        return instance.getFunction().apply(a, b);
    }
}

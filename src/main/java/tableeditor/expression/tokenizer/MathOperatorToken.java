package tableeditor.expression.tokenizer;

import tableeditor.expression.dso.MathOperation;
import tableeditor.expression.exception.ExpressionException;

import java.math.BigDecimal;
import java.util.List;

public class MathOperatorToken implements Token, OperationToken<MathOperation> {
    private final MathOperation operation;

    public MathOperatorToken(MathOperation instance) {
        this.operation = instance;
    }

    @Override
    public String getValue() {
        return operation.getName();
    }

    public MathOperation getOperation() {
        return operation;
    }

}

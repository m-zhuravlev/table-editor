package tableeditor.expression.tokenizer;

import tableeditor.expression.dso.CompareOperation;

public class CompareToken implements Token, OperationToken<CompareOperation> {
    private final CompareOperation operation;

    public CompareToken(CompareOperation operation) {
        this.operation = operation;
    }

    @Override
    public String getValue() {
        return operation.getName();
    }

    @Override
    public CompareOperation getOperation() {
        return operation;
    }
}

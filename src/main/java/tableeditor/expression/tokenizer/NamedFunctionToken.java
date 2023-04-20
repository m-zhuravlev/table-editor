package tableeditor.expression.tokenizer;

import tableeditor.expression.dso.NamedOperation;

public class NamedFunctionToken implements Token, TerminalToken, OperationToken<NamedOperation> {
    private final NamedOperation operation;

    public NamedFunctionToken(NamedOperation op) {
        this.operation = op;
    }

    @Override
    public String getValue() {
        return operation.getName();
    }

    public int getParamsCount() {
        return operation.inputParams().size();
    }

    @Override
    public NamedOperation getOperation() {
        return operation;
    }
}

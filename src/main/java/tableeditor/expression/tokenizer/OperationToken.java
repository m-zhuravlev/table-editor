package tableeditor.expression.tokenizer;

import tableeditor.expression.dso.Operation;

public interface OperationToken<T extends Operation> {

    T getOperation();

    default int getPriority(){
        return getOperation().priority();
    }
}

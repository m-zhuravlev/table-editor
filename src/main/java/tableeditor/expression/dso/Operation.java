package tableeditor.expression.dso;

import tableeditor.expression.exception.ExpressionException;

import java.util.List;
import java.util.function.Function;

public interface Operation {

    String getName();

    int priority();

    List<Class<?>> inputParams();

    Class<?> returnParam();

    Function getFunction();

    default Object execute(List<Object> input) throws ExpressionException {
        for (int i = 0; i < inputParams().size(); i++) {
            if (!inputParams().get(i).isInstance(input.get(i))) {
                throw new ExpressionException("16: Incompatible input parameter type for binary operation " + getName());
            }
        }
        return getFunction().apply(input);
    }
}

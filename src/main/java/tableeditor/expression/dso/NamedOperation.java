package tableeditor.expression.dso;

import java.util.List;
import java.util.function.Function;

public class NamedOperation implements Operation {
    private final String name;
    private final List<Class<?>> inputParams;
    private final Class<?> returnParam;
    private final Function<List<Object>, Object> function;

    public NamedOperation(String name, List<Class<?>> inputParams, Class<?> returnParam, Function<List<Object>, Object> function) {
        this.name = name;
        this.inputParams = inputParams;
        this.returnParam = returnParam;
        this.function = function;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public List<Class<?>> inputParams() {
        return inputParams;
    }

    @Override
    public Class<?> returnParam() {
        return returnParam;
    }

    @Override
    public  Function<List<Object>,Object> getFunction() {
        return function;
    }
}

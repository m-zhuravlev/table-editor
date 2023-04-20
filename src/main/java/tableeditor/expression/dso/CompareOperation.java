package tableeditor.expression.dso;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class CompareOperation implements Operation {

    private static final List<Class<?>> inputParams = List.of(BigDecimal.class, BigDecimal.class);
    private final String name;
    private final Function<List<BigDecimal>, Boolean> function;

    public CompareOperation(String name, Function<List<BigDecimal>, Boolean> function) {
        this.name = name;
        this.function = function;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public List<Class<?>> inputParams() {
        return inputParams;
    }

    @Override
    public Class<?> returnParam() {
        return Boolean.class;
    }

    @Override
    public Function<List<BigDecimal>, Boolean> getFunction() {
        return function;
    }


}

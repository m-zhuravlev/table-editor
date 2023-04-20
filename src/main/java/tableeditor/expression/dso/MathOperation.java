package tableeditor.expression.dso;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class MathOperation implements Operation {
    private static final List<Class<?>> inputParams = List.of(BigDecimal.class, BigDecimal.class);
    private final char ch;
    private final int priority;
    private final Function<List<BigDecimal>, BigDecimal> function;

    public MathOperation(char ch, int priority, Function<List<BigDecimal>, BigDecimal> function) {
        this.ch = ch;
        this.priority = priority;
        this.function = function;
    }

    @Override
    public String getName() {
        return String.valueOf(ch);
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public List<Class<?>> inputParams() {
        return inputParams;
    }

    @Override
    public Class<?> returnParam() {
        return BigDecimal.class;
    }

    @Override
    public Function<List<BigDecimal>, BigDecimal> getFunction() {
        return function;
    }
}

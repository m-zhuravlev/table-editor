package tableeditor.expression.dso;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public class UnaryOperation implements Operation {
    private static final List<Class<?>> inputParams = List.of(BigDecimal.class);
    private final char ch;
    private final Function<List<BigDecimal>, BigDecimal> function;

    public UnaryOperation(char ch, Function<List<BigDecimal>, BigDecimal> function) {
        this.ch = ch;
        this.function = function;
    }

    @Override
    public String getName() {
        return String.valueOf(ch);
    }

    @Override
    public int priority() {
        return 4;
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

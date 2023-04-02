package tableeditor.expression.tokenizer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static tableeditor.expression.Constants.MATH_CONTEXT;

public enum FunctionEnum {
    POW("pow", 2, BigDecimal.class, (l) -> (l.get(0)).pow(l.get(1).intValue(), MATH_CONTEXT));

    public final String functionName;
    public final int inputParamsCount;
    public final Class<?> returnType;
    private final Function<List<BigDecimal>, BigDecimal> function;

    FunctionEnum(String name, int inputParamsCount, Class<?> returnType, Function<List<BigDecimal>, BigDecimal> f) {
        this.functionName = name;
        this.inputParamsCount = inputParamsCount;
        this.returnType = returnType;
        function = f;
    }

    public static FunctionEnum getByName(String name) {
        return Arrays.stream(values())
                .filter(v -> v.functionName.equals(name))
                .findFirst()
                .orElse(null);
    }

    public Function<List<BigDecimal>, BigDecimal> getFunction() {
        return function;
    }
}

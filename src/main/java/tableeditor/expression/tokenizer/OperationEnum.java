package tableeditor.expression.tokenizer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.BiFunction;

import static tableeditor.expression.Constants.MATH_CONTEXT;

public enum OperationEnum {
    PLUS('+', 1, (bigDecimal, augend) -> bigDecimal.add(augend, MATH_CONTEXT)),
    MINUS('-', 1, (bigDecimal, subtrahend) -> bigDecimal.subtract(subtrahend, MATH_CONTEXT)),
    MULTIPLY('*', 2, (bigDecimal, multiplicand) -> bigDecimal.multiply(multiplicand, MATH_CONTEXT)),
    DIVIDE('/', 2, (bigDecimal, divisor) -> bigDecimal.divide(divisor, MATH_CONTEXT)),
    REMAINDER('%', 2, (bigDecimal, divisor) -> bigDecimal.remainder(divisor, MATH_CONTEXT));

    public final char charValue;
    public final int priority;
    private final BiFunction<BigDecimal, BigDecimal, BigDecimal> function;

    OperationEnum(char ch, int priority, BiFunction<BigDecimal, BigDecimal, BigDecimal> f) {
        charValue = ch;
        this.priority = priority;
        function = f;
    }

    public static boolean isOperation(char ch) {
        return Arrays.stream(values()).anyMatch(e -> e.charValue == ch);
    }

    public static OperationEnum getByChar(char ch) {
        return Arrays.stream(values())
                .filter(v -> v.charValue == ch)
                .findFirst()
                .orElseThrow();
    }

    public BiFunction<BigDecimal, BigDecimal, BigDecimal> getFunction() {
        return function;
    }

}

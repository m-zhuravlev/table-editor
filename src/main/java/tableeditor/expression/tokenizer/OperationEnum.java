package tableeditor.expression.tokenizer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.BiFunction;

public enum OperationEnum {
    PLUS('+', 1, BigDecimal::add),
    MINUS('-', 1, BigDecimal::subtract),
    MULTIPLY('*', 2, BigDecimal::multiply),
    DIVIDE('/', 2, BigDecimal::divide),
    REMAINDER('%', 2, BigDecimal::remainder);

    public final char charValue;
    public final int priority;
    private final BiFunction<BigDecimal,BigDecimal,BigDecimal> function;

    OperationEnum(char ch, int priority, BiFunction<BigDecimal,BigDecimal,BigDecimal> f) {
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

    public BiFunction<BigDecimal,BigDecimal,BigDecimal> getFunction() {
        return function;
    }
}

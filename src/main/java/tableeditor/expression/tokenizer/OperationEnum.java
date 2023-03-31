package tableeditor.expression.tokenizer;

import java.util.Arrays;

public enum OperationEnum {
    PLUS('+', 1),
    MINUS('-', 1),
    MULTIPLY('*', 2),
    DIVIDE('/', 2),
    REMAINDER('%', 2);

    public final char charValue;
    public final int priority;

    OperationEnum(char ch, int priority) {
        charValue = ch;
        this.priority = priority;
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
}

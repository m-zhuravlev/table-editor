package tableeditor.expression.tokenizer;

import java.math.BigDecimal;
import java.util.Arrays;

public enum FunctionEnum {
    POW("pow", 2, BigDecimal.class);

    public final String functionName;
    public final int inputParamsCount;
    public final Class<?> returnType;

    FunctionEnum(String name, int inputParamsCount, Class<?> returnType) {
        this.functionName = name;
        this.inputParamsCount = inputParamsCount;
        this.returnType = returnType;
    }

    public static FunctionEnum getByName(String name) {
        return Arrays.stream(values())
                .filter(v -> v.functionName.equals(name))
                .findFirst()
                .orElse(null);
    }
}

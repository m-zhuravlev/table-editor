package tableeditor.expression.tokenizer;

import tableeditor.expression.enums.FunctionEnum;

import java.math.BigDecimal;
import java.util.List;

public class NamedFunctionToken implements Token, TerminalToken {
    private final FunctionEnum instance;

    public NamedFunctionToken(FunctionEnum fun) {
        this.instance = fun;
    }

    @Override
    public String getValue() {
        return instance.functionName;
    }

    public int getParamsCount() {
        return instance.inputParamsCount;
    }

    public BigDecimal execute(List<BigDecimal> input) {
        return instance.getFunction().apply(input);
    }
}

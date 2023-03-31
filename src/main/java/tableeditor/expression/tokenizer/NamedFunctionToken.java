package tableeditor.expression.tokenizer;

public class NamedFunctionToken implements Token, TerminalToken {
    private final FunctionEnum fun;

    public NamedFunctionToken(FunctionEnum fun) {
        this.fun = fun;
    }

    @Override
    public String getValue() {
        return fun.functionName;
    }

    public int getParamsCount() {
        return fun.inputParamsCount;
    }
}

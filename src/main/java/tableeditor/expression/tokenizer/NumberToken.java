package tableeditor.expression.tokenizer;

import java.math.BigDecimal;

public record NumberToken(String value) implements Token, TerminalToken {

    public BigDecimal getNumberValue() {
        return new BigDecimal(value);
    }

    @Override
    public String getValue() {
        return value();
    }
}

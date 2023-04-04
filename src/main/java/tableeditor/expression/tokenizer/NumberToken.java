package tableeditor.expression.tokenizer;

import tableeditor.expression.exception.ExpressionException;

import java.math.BigDecimal;

public record NumberToken(String value) implements Token, TerminalToken {

    public BigDecimal getNumberValue() throws ExpressionException {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new ExpressionException("6: Error. cannot cast to Number value '" + value + "'");
        }
    }

    @Override
    public String getValue() {
        return value();
    }
}

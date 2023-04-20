package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.expression.exception.ExpressionException;
import tableeditor.expression.tokenizer.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {
    @Test
    public void simpleTest() throws ExpressionException {
        List<Token> l = Tokenizer.generateTokens("2+2* 3 % (10.2- 5.2)");
        assertEquals(l.get(0).getValue(), "2");
        assertEquals(l.get(1).getValue(), "+");
        assertEquals(l.get(2).getValue(), "2");
        assertEquals(l.get(3).getValue(), "*");
        assertEquals(l.get(4).getValue(), "3");
        assertEquals(l.get(5).getValue(), "%");
        assertEquals(l.get(6).getValue(), "(");
        assertEquals(l.get(7).getValue(), "10.2");
        assertEquals(l.get(8).getValue(), "-");
        assertEquals(l.get(9).getValue(), "5.2");
        assertEquals(l.get(10).getValue(), ")");
    }

    @Test
    public void funcTest() throws ExpressionException {
        List<Token> l = Tokenizer.generateTokens("pow(2, A1) + 42");
        assertEquals(l.get(0).getValue(), "pow");
        assertEquals(l.get(1).getValue(), "(");
        assertEquals(l.get(2).getValue(), "2");
        assertEquals(l.get(3).getValue(), ",");
        assertEquals(l.get(4).getValue(), "A1");
        assertEquals(l.get(5).getValue(), ")");
        assertEquals(l.get(6).getValue(), "+");
        assertEquals(l.get(7).getValue(), "42");
    }

    @Test
    public void typeTest() throws ExpressionException {
        List<Token> l = Tokenizer.generateTokens("pow(5.3+2, A1)");
        assertTrue(l.get(0) instanceof NamedFunctionToken);
        assertTrue(l.get(1) instanceof BracketOpenToken);
        assertTrue(l.get(2) instanceof NumberToken);
        assertTrue(l.get(3) instanceof MathOperatorToken);
        assertTrue(l.get(4) instanceof NumberToken);
        assertTrue(l.get(5) instanceof CommaToken);
        assertTrue(l.get(6) instanceof CellLinkToken);
        assertTrue(l.get(7) instanceof BracketCloseToken);
    }

    @Test
    public void compareOpTest() throws ExpressionException {
        List<Token> l = Tokenizer.generateTokens("2>1");
        assertEquals(l.get(0).getValue(), "2");
        assertEquals(l.get(1).getValue(), ">");
        assertInstanceOf(CompareToken.class, l.get(1));
        assertEquals(l.get(2).getValue(), "1");

        l = Tokenizer.generateTokens("2>=1");
        assertEquals(l.get(0).getValue(), "2");
        assertEquals(l.get(1).getValue(), ">=");
        assertEquals(l.get(2).getValue(), "1");
    }

    @Test
    public void errorTest() {
        assertThrows(ExpressionException.class, () -> Tokenizer.generateTokens("())"));
        assertThrows(ExpressionException.class, () -> Tokenizer.generateTokens("1 + unknownFunc()"));
        assertThrows(ExpressionException.class, () -> Tokenizer.generateTokens("2 + , 3"));
        assertThrows(ExpressionException.class, () -> Tokenizer.generateTokens("1 2"));
        assertThrows(ExpressionException.class, () -> Tokenizer.generateTokens("1.    2"));
        assertThrows(ExpressionException.class, () -> Tokenizer.generateTokens("5 pow(1,2)"));
    }


}

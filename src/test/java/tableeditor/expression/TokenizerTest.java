package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.expression.tokenizer.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TokenizerTest {
    @Test
    public void simpleTest() {
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
    public void funcTest() {
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
    public void typeTest() {
        List<Token> l = Tokenizer.generateTokens("pow(5.3+2, A1)");
        assertTrue(l.get(0) instanceof NamedFunctionToken);
        assertTrue(l.get(1) instanceof BracketToken);
        assertTrue(l.get(2) instanceof NumberToken);
        assertTrue(l.get(3) instanceof OperatorToken);
        assertTrue(l.get(4) instanceof NumberToken);
        assertTrue(l.get(5) instanceof CommaToken);
        assertTrue(l.get(6) instanceof CellLinkToken);
        assertTrue(l.get(7) instanceof BracketToken);
    }

    @Test
    public void errorTest() {
        assertThrows(IllegalArgumentException.class, () -> Tokenizer.generateTokens("())"));
        assertThrows(IllegalArgumentException.class, () -> Tokenizer.generateTokens("1 + unknownFunc()"));
        assertThrows(IllegalArgumentException.class, () -> Tokenizer.generateTokens("2 + , 3"));
        assertThrows(IllegalArgumentException.class, () -> Tokenizer.generateTokens("1 2"));
        assertThrows(IllegalArgumentException.class, () -> Tokenizer.generateTokens("1.    2"));
        assertThrows(IllegalArgumentException.class, () -> Tokenizer.generateTokens("5 pow(1,2)"));
    }


}

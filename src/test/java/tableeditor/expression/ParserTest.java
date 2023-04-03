package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.expression.exception.ExpressionException;
import tableeditor.expression.parser.Parser;
import tableeditor.expression.parser.TokenNode;
import tableeditor.expression.tokenizer.Tokenizer;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void simpleTest() throws ExpressionException {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("1"));
        assertEquals(root.shiftEmpty().getToken().getValue(), "1");

        root = Parser.parseTokens(Tokenizer.generateTokens("1+2"));
        assertEquals(root.getToken().getValue(), "+");
        assertEquals(root.getLeft().getToken().getValue(), "1");
        assertEquals(root.getRight().getToken().getValue(), "2");

        root = Parser.parseTokens(Tokenizer.generateTokens("(((1+2)))"));
        assertNull(root.getToken());
        assertNull(root.getLeft().getToken());
        assertNull(root.getLeft().getLeft().getToken());
        TokenNode localRoot = root.getLeft().getLeft().getLeft();
        assertEquals(localRoot.getToken().getValue(), "+");
        assertEquals(localRoot.getLeft().getToken().getValue(), "1");
        assertEquals(localRoot.getRight().getToken().getValue(), "2");

        root = Parser.parseTokens(Tokenizer.generateTokens("( 3+ ( 4 - 5 ))")).shiftEmpty();
        assertEquals(root.getToken().getValue(), "+");
        assertEquals(root.getLeft().getToken().getValue(), "3");
        assertEquals(root.getRight().getToken().getValue(), "-");
        assertEquals(root.getRight().getLeft().getToken().getValue(), "4");
        assertEquals(root.getRight().getRight().getToken().getValue(), "5");

        root = Parser.parseTokens(Tokenizer.generateTokens("3+4*5"));
        assertEquals(root.getToken().getValue(), "+");
        assertEquals(root.getLeft().getToken().getValue(), "3");
        assertEquals(root.getRight().getToken().getValue(), "*");
        assertEquals(root.getRight().getLeft().getToken().getValue(), "4");
        assertEquals(root.getRight().getRight().getToken().getValue(), "5");

        root = Parser.parseTokens(Tokenizer.generateTokens("(6-2)*3"));
        assertEquals(root.getToken().getValue(), "*");
    }


    @Test
    public void cellLinkTest() throws ExpressionException {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("A1"));
        assertNull(root.getToken());
        assertEquals(root.getLeft().getToken().getValue(), "A1");

        root = Parser.parseTokens(Tokenizer.generateTokens("1+A2"));
        assertEquals(root.getToken().getValue(), "+");
        assertEquals(root.getLeft().getToken().getValue(), "1");
        assertEquals(root.getRight().getToken().getValue(), "A2");

        root = Parser.parseTokens(Tokenizer.generateTokens("(1)+(A2)"));
        assertEquals(root.getToken().getValue(), "+");
        assertNull(root.getLeft().getToken());
        assertEquals(root.getLeft().getLeft().getToken().getValue(), "1");
        assertNull(root.getRight().getToken());
        assertEquals(root.getRight().getLeft().getToken().getValue(), "A2");

    }

    @Test
    public void namedFuncTest() throws ExpressionException {
        TokenNode root;
        assertThrows(ExpressionException.class, () -> Parser.parseTokens(Tokenizer.generateTokens("pow()")));
        assertThrows(ExpressionException.class, () -> Parser.parseTokens(Tokenizer.generateTokens("pow(1)")));
        assertThrows(ExpressionException.class, () -> Parser.parseTokens(Tokenizer.generateTokens("pow(1,2,3)")));

        root = Parser.parseTokens(Tokenizer.generateTokens("pow(2,3)")).shiftEmpty();
        assertEquals(root.getToken().getValue(), "pow");
        assertEquals(root.getParams().get(0).shiftEmpty().getToken().getValue(), "2");
        assertEquals(root.getParams().get(1).shiftEmpty().getToken().getValue(), "3");

        root = Parser.parseTokens(Tokenizer.generateTokens("pow( 1.1+2.2 , pow(3,4) )")).shiftEmpty();
        assertEquals(root.getToken().getValue(), "pow");
        assertEquals(root.getParams().get(0).shiftEmpty().getToken().getValue(), "+");
        assertEquals(root.getParams().get(0).shiftEmpty().getLeft().getToken().getValue(), "1.1");
        assertEquals(root.getParams().get(0).shiftEmpty().getRight().getToken().getValue(), "2.2");
        assertEquals(root.getParams().get(1).shiftEmpty().getToken().getValue(), "pow");
        assertEquals(root.getParams().get(1).shiftEmpty().getParams().get(0).shiftEmpty().getToken().getValue(), "3");
        assertEquals(root.getParams().get(1).shiftEmpty().getParams().get(1).shiftEmpty().getToken().getValue(), "4");

        root = Parser.parseTokens(Tokenizer.generateTokens("7.7*pow(2,3)"));
        assertEquals(root.getToken().getValue(), "*");
        assertEquals(root.getLeft().getToken().getValue(), "7.7");
        assertEquals(root.getRight().getToken().getValue(), "pow");
        assertEquals(root.getRight().getParams().get(0).shiftEmpty().getToken().getValue(), "2");
        assertEquals(root.getRight().getParams().get(1).shiftEmpty().getToken().getValue(), "3");

    }

    @Test
    public void unaryOperationTest() throws ExpressionException {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("-1-2"));
        assertEquals(root.getToken().getValue(), "-");
        assertEquals(root.getRight().getToken().getValue(), "2");
        assertEquals(root.getLeft().getToken().getValue(), "-");
        assertEquals(root.getLeft().getRight().getToken().getValue(), "1");
    }
}

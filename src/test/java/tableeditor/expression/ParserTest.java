package tableeditor.expression;

import org.junit.jupiter.api.Test;
import tableeditor.expression.parser.Parser;
import tableeditor.expression.parser.TokenNode;
import tableeditor.expression.tokenizer.Tokenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ParserTest {

    @Test
    public void simpleTest() {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("1"));
        assertEquals(root.getToken().getValue(), "1");

        root = Parser.parseTokens(Tokenizer.generateTokens("1+2"));
        assertEquals(root.getToken().getValue(), "+");
        assertEquals(root.getLeft().getToken().getValue(), "1");
        assertEquals(root.getRight().getToken().getValue(), "2");

        root = Parser.parseTokens(Tokenizer.generateTokens("(((1+2)))"));
        assertEquals(root.getToken().getValue(), "+");
        assertEquals(root.getLeft().getToken().getValue(), "1");
        assertEquals(root.getRight().getToken().getValue(), "2");

        root = Parser.parseTokens(Tokenizer.generateTokens("( 3+ ( 4 - 5 ))"));
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
    }


    @Test
    public void cellLinkTest() {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("A1"));
        assertEquals(root.getToken().getValue(), "A1");

        root = Parser.parseTokens(Tokenizer.generateTokens("1+A2"));
        assertEquals(root.getToken().getValue(), "+");
        assertEquals(root.getLeft().getToken().getValue(), "1");
        assertEquals(root.getRight().getToken().getValue(), "A2");

    }

    @Test
    public void namedFuncTest() {
        TokenNode root;
        assertThrows(IllegalArgumentException.class, () -> Parser.parseTokens(Tokenizer.generateTokens("pow()")));
        assertThrows(IllegalArgumentException.class, () -> Parser.parseTokens(Tokenizer.generateTokens("pow(1)")));
        assertThrows(IllegalArgumentException.class, () -> Parser.parseTokens(Tokenizer.generateTokens("pow(1,2,3)")));

        root = Parser.parseTokens(Tokenizer.generateTokens("pow(2,3)"));
        assertEquals(root.getToken().getValue(), "pow");
        assertEquals(root.getParams().get(0).getToken().getValue(), "2");
        assertEquals(root.getParams().get(1).getToken().getValue(), "3");

        root = Parser.parseTokens(Tokenizer.generateTokens("pow( 1.1+2.2 , pow(3,4) )"));
        assertEquals(root.getToken().getValue(), "pow");
        assertEquals(root.getParams().get(0).getToken().getValue(), "+");
        assertEquals(root.getParams().get(0).getLeft().getToken().getValue(), "1.1");
        assertEquals(root.getParams().get(0).getRight().getToken().getValue(), "2.2");
        assertEquals(root.getParams().get(1).getToken().getValue(), "pow");
        assertEquals(root.getParams().get(1).getParams().get(0).getToken().getValue(), "3");
        assertEquals(root.getParams().get(1).getParams().get(1).getToken().getValue(), "4");

        root = Parser.parseTokens(Tokenizer.generateTokens("7.7*pow(2,3)"));
        assertEquals(root.getToken().getValue(), "*");
        assertEquals(root.getLeft().getToken().getValue(), "7.7");
        assertEquals(root.getRight().getToken().getValue(), "pow");
        assertEquals(root.getRight().getParams().get(0).getToken().getValue(), "2");
        assertEquals(root.getRight().getParams().get(1).getToken().getValue(), "3");

    }
}

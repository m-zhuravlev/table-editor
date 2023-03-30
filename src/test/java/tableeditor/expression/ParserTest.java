package tableeditor.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tableeditor.expression.parser.Parser;
import tableeditor.expression.parser.TokenNode;
import tableeditor.expression.tokenizer.Tokenizer;

public class ParserTest {

    @Test
    public void simpleTest() {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("1"));
        Assertions.assertEquals(root.getToken().getValue(), "1");

        root = Parser.parseTokens(Tokenizer.generateTokens("1+2"));
        Assertions.assertEquals(root.getToken().getValue(), "+");
        Assertions.assertEquals(root.getLeft().getToken().getValue(), "1");
        Assertions.assertEquals(root.getRight().getToken().getValue(), "2");

        root = Parser.parseTokens(Tokenizer.generateTokens("(((1+2)))"));
        Assertions.assertEquals(root.getToken().getValue(), "+");
        Assertions.assertEquals(root.getLeft().getToken().getValue(), "1");
        Assertions.assertEquals(root.getRight().getToken().getValue(), "2");

        root = Parser.parseTokens(Tokenizer.generateTokens("( 3+ ( 4 - 5 ))"));
        Assertions.assertEquals(root.getToken().getValue(), "+");
        Assertions.assertEquals(root.getLeft().getToken().getValue(), "3");
        Assertions.assertEquals(root.getRight().getToken().getValue(), "-");
        Assertions.assertEquals(root.getRight().getLeft().getToken().getValue(), "4");
        Assertions.assertEquals(root.getRight().getRight().getToken().getValue(), "5");

        root = Parser.parseTokens(Tokenizer.generateTokens("3+4*5"));
        Assertions.assertEquals(root.getToken().getValue(), "+");
        Assertions.assertEquals(root.getLeft().getToken().getValue(), "3");
        Assertions.assertEquals(root.getRight().getToken().getValue(), "*");
        Assertions.assertEquals(root.getRight().getLeft().getToken().getValue(), "4");
        Assertions.assertEquals(root.getRight().getRight().getToken().getValue(), "5");
    }


    @Test
    public void cellLinkTest() {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("A1"));
        Assertions.assertEquals(root.getToken().getValue(), "A1");

        root = Parser.parseTokens(Tokenizer.generateTokens("1+A2"));
        Assertions.assertEquals(root.getToken().getValue(), "+");
        Assertions.assertEquals(root.getLeft().getToken().getValue(), "1");
        Assertions.assertEquals(root.getRight().getToken().getValue(), "A2");

    }

    @Test
    public void namedFuncTest() {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens("pow"));
        Assertions.assertEquals(root.getToken().getValue(), "pow");

        root = Parser.parseTokens(Tokenizer.generateTokens("pow(2,3)"));
        Assertions.assertEquals(root.getToken().getValue(), "pow");
        Assertions.assertEquals(root.getLeft().getToken().getValue(), "2");
        Assertions.assertEquals(root.getRight().getToken().getValue(), "3");

    }
}

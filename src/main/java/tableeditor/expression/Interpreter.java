package tableeditor.expression;

import tableeditor.expression.parser.Parser;
import tableeditor.expression.parser.TokenNode;
import tableeditor.expression.tokenizer.*;

import java.math.BigDecimal;
import java.util.List;

public class Interpreter {


    public static BigDecimal getResult(String inputString) {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens(inputString));
        return evaluate(root);
    }

    private static BigDecimal evaluate(TokenNode node) {
        if (node.getToken() instanceof NumberToken) {
            return ((NumberToken) node.getToken()).getNumberValue();
        } else if (node.getToken() instanceof CellLinkToken) {
//          TODO  return ((CellLinkToken)node.getToken()).resolveValue();
        } else if (node.getToken() instanceof OperatorToken) {
            return ((OperatorToken) node.getToken()).execute(evaluate(node.getLeft()), evaluate(node.getRight()));
        } else if (node.getToken() instanceof NamedFunctionToken) {
            List<BigDecimal> input = node.getParams().stream()
                    .map(Interpreter::evaluate)
                    .toList();
            return ((NamedFunctionToken) node.getToken()).execute(input);
        }
        return null;
    }
}

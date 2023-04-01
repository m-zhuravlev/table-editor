package tableeditor.expression;

import tableeditor.expression.parser.Parser;
import tableeditor.expression.parser.TokenNode;
import tableeditor.expression.tokenizer.*;
import tableeditor.ui.MyTableModel;

import java.math.BigDecimal;
import java.util.List;

public class Interpreter {

    private static MyTableModel tableModel;

    public static BigDecimal getResult(String inputString) {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens(inputString));
        return evaluate(root);
    }

    private static BigDecimal evaluate(TokenNode node) {
        if (node == null) return null;
        node = node.shiftEmpty();
        if (node.getToken() instanceof NumberToken) {
            return ((NumberToken) node.getToken()).getNumberValue();
        } else if (node.getToken() instanceof CellLinkToken) {
            return ((CellLinkToken) node.getToken()).resolveValue(tableModel);
        } else if (node.getToken() instanceof OperatorToken) {
            return executeOperation(node);
        } else if (node.getToken() instanceof NamedFunctionToken) {
            List<BigDecimal> input = node.getParams().stream().map(Interpreter::evaluate).toList();
            return ((NamedFunctionToken) node.getToken()).execute(input);
        }
        return null;
    }

    private static BigDecimal executeOperation(TokenNode node) {
        OperatorToken token = (OperatorToken) node.getToken();
        if (node.getLeft() == null) {
            if (token.getInstance() == OperationEnum.MINUS) {
                return evaluate(node.getRight()).negate();
            }
            if (token.getInstance() == OperationEnum.PLUS) {
                return evaluate(node.getRight());
            } else throw new UnsupportedOperationException("Unsupported unary operation " + token.getValue());
        } else {
            return token.execute(evaluate(node.getLeft()), evaluate(node.getRight()));
        }
    }

    public static void setTableModel(MyTableModel tableModel) {
        Interpreter.tableModel = tableModel;
    }
}

package tableeditor.expression;

import tableeditor.expression.enums.OperationEnum;
import tableeditor.expression.exception.ExpressionException;
import tableeditor.expression.parser.Parser;
import tableeditor.expression.parser.TokenNode;
import tableeditor.expression.tokenizer.*;
import tableeditor.ui.CellModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private final CellModel cellModel;

    public Interpreter(CellModel cellModel) {
        this.cellModel = cellModel;
    }

    public BigDecimal getResult(String inputString) throws ExpressionException {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens(inputString));
        return evaluate(root);
    }

    private BigDecimal evaluate(TokenNode node) throws ExpressionException {
        if (node == null) return null;
        node = node.shiftEmpty();
        if (node.getToken() instanceof NumberToken) {
            return ((NumberToken) node.getToken()).getNumberValue();
        } else if (node.getToken() instanceof CellLinkToken) {
            return ((CellLinkToken) node.getToken()).resolveValue(cellModel);
        } else if (node.getToken() instanceof OperatorToken) {
            return executeOperation(node);
        } else if (node.getToken() instanceof NamedFunctionToken) {
            List<BigDecimal> input = new ArrayList<>();
            for (TokenNode tokenNode : node.getParams()) {
                input.add(evaluate(tokenNode));
            }
            return ((NamedFunctionToken) node.getToken()).execute(input);
        } else throw new ExpressionException("Unknown operation ");
    }

    private BigDecimal executeOperation(TokenNode node) throws ExpressionException {
        OperatorToken token = (OperatorToken) node.getToken();
        if (node.getLeft() == null) {
            if (token.getInstance() == OperationEnum.MINUS) {
                return evaluate(node.getRight()).negate();
            }
            if (token.getInstance() == OperationEnum.PLUS) {
                return evaluate(node.getRight());
            } else throw new ExpressionException("Unsupported unary operation " + token.getValue());
        } else if (node.getRight() == null || node.getRight().shiftEmpty().getToken() == null) {
            throw new ExpressionException("Second operand binary operation '" + token.getValue() + "' not defined");
        } else {
            return token.execute(evaluate(node.getLeft()), evaluate(node.getRight()));
        }
    }
}

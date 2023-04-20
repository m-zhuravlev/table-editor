package tableeditor.expression;

import tableeditor.expression.dso.DSO;
import tableeditor.expression.dso.Operation;
import tableeditor.expression.dso.UnaryOperation;
import tableeditor.expression.exception.ExpressionException;
import tableeditor.expression.parser.Parser;
import tableeditor.expression.parser.TokenNode;
import tableeditor.expression.tokenizer.*;
import tableeditor.ui.CellModel;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private final CellModel cellModel;

    public Interpreter(CellModel cellModel) {
        this.cellModel = cellModel;
    }

    public Object getResult(String inputString) throws ExpressionException {
        TokenNode root = Parser.parseTokens(Tokenizer.generateTokens(inputString));
        return evaluate(root);
    }

    private Object evaluate(TokenNode node) throws ExpressionException {
        if (node == null) return null;
        node = node.shiftEmpty();
        if (node.getToken() instanceof NumberToken) {
            return ((NumberToken) node.getToken()).getNumberValue();
        } else if (node.getToken() instanceof CellLinkToken) {
            return ((CellLinkToken) node.getToken()).resolveValue(cellModel);
        } else if (node.getToken() instanceof MathOperatorToken) {
            return executeMathOperation(node);
        } else if (node.getToken() instanceof OperationToken<?> token) {
            return executeOperation(node.getParams(), token.getOperation());
        } else throw new ExpressionException("12: Unknown operation ");
    }

    private Object executeOperation(List<TokenNode> params, Operation operation) throws ExpressionException {
        List<Object> input = new ArrayList<>();
        for (TokenNode tokenNode : params) {
            input.add(evaluate(tokenNode));
        }
        return operation.execute(input);
    }

    private Object executeMathOperation(TokenNode node) throws ExpressionException {
        MathOperatorToken token = (MathOperatorToken) node.getToken();
        if (node.getLeft() == null) {
            return executeUnaryOperation(token, node.getRight());
        } else if (node.getRight() == null || node.getRight().shiftEmpty().getToken() == null) {
            throw new ExpressionException("14: Second operand binary operation '" + token.getValue() + "' not defined");
        } else {
            return executeOperation(node.getParams(), token.getOperation());
        }
    }

    private Object executeUnaryOperation(MathOperatorToken token, TokenNode right) throws ExpressionException {
        UnaryOperation unaryOP = DSO.unaryOp.get(token.getValue());
        if (unaryOP == null) throw new ExpressionException("13: Unsupported unary operation " + token.getValue());
        return executeOperation(List.of(right.shiftEmpty()), unaryOP);
    }
}

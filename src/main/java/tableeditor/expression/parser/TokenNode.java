package tableeditor.expression.parser;

import tableeditor.expression.tokenizer.NamedFunctionToken;
import tableeditor.expression.tokenizer.Token;

import java.util.ArrayList;
import java.util.List;

public class TokenNode {

    private Token token;

    private List<TokenNode> params = new ArrayList<>();

    public TokenNode(Token token) {
        this.token = token;
        params.add(null);
        params.add(null);
    }

    public TokenNode() {
        this(null);
    }

    public TokenNode getLeft() {
        return params.get(0);
    }

    public void setLeft(TokenNode left) {
        params.set(0, left);
    }

    public TokenNode getRight() {
        return params.get(1);
    }

    public void setRight(TokenNode right) {
        params.set(1, right);
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<TokenNode> getParams() {
        return params;
    }

    public void setParams(List<TokenNode> params) {
        this.params = params;
    }

    public void checkParams() {
        if (token instanceof NamedFunctionToken) {
            if (params.size() != ((NamedFunctionToken) token).getParamsCount())
                throw new IllegalArgumentException("Function %s should have %d input params".formatted(token.getValue(), ((NamedFunctionToken) token).getParamsCount()));
        }

    }

    public TokenNode shiftEmpty() {
        TokenNode node = this;
        while (node.getToken() == null) {
            node = node.getLeft();
        }
        return node;
    }
}

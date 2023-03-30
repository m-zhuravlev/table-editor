package tableeditor.expression.parser;

import tableeditor.expression.tokenizer.Token;

public class TokenNode {

    private TokenNode left;
    private TokenNode right;
    private Token token;

    public TokenNode(Token token) {
        this.token = token;
    }

    public TokenNode() {
        token = null;
    }

    public TokenNode getLeft() {
        return left;
    }

    public void setLeft(TokenNode left) {
        this.left = left;
    }

    public TokenNode getRight() {
        return right;
    }

    public void setRight(TokenNode right) {
        this.right = right;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}

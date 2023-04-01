package tableeditor.expression.parser;

import tableeditor.expression.tokenizer.*;

import java.util.*;

public class Parser {
    public static TokenNode parseTokens(List<Token> tokens) throws IllegalArgumentException {
        TokenNode cur = new TokenNode();
        Deque<TokenNode> stack = new LinkedList<>();
        stack.push(cur);
        cur.setLeft(new TokenNode());
        cur = cur.getLeft();
        Iterator<Token> it = tokens.iterator();
        while (it.hasNext()) {
            Token token = it.next();
            if (token instanceof BracketOpenToken) {
                stack.push(cur);
                cur.setLeft(new TokenNode());
                cur = cur.getLeft();
            } else if (token instanceof TerminalToken) {
                cur = parseTerminalToken(cur, stack, token, it);
            } else if (token instanceof OperatorToken) {
                cur = parseOperator(cur, stack, token);
            } else if (token instanceof BracketCloseToken) {
                cur = stack.pop();
            } else throw new UnsupportedOperationException("Syntax error " + token.getValue());
        }

        while (!stack.isEmpty()) {
            cur = stack.pop();
        }
        return cur;
    }

    private static TokenNode parseTerminalToken(TokenNode cur, Deque<TokenNode> stack, Token token, Iterator<Token> it) {
        cur.setToken(token);
        if (token instanceof NamedFunctionToken) {
            cur.setParams(parseFunctionParams(it));
            cur.checkParams();
        }
        if (stack.isEmpty()) {
            TokenNode node = new TokenNode();
            node.setLeft(cur);
            cur = node;
        } else {
            cur = stack.pop();
        }
        return cur;
    }

    private static List<TokenNode> parseFunctionParams(Iterator<Token> it) {
        List<TokenNode> params = new ArrayList<>();
        int brktCount = 0;
        Token token = it.next();
        if (token instanceof BracketOpenToken) brktCount++;
        List<Token> tokenList = new ArrayList<>();
        while (it.hasNext() && brktCount > 0) {
            token = it.next();
            if (token instanceof BracketOpenToken) {
                brktCount++;
                tokenList.add(token);
            } else if (token instanceof BracketCloseToken) {
                brktCount--;
                if (brktCount > 0) {
                    tokenList.add(token);
                } else if (!tokenList.isEmpty()) {
                    params.add(parseTokens(tokenList));
                }
            } else if (token instanceof CommaToken) {
                if (brktCount > 1) {
                    tokenList.add(token);
                } else {
                    params.add(parseTokens(tokenList));
                    tokenList.clear();
                }
            } else {
                tokenList.add(token);
            }
        }
        return params;
    }

    private static TokenNode parseOperator(TokenNode cur, Deque<TokenNode> stack, Token token) {
        if (cur.getToken() instanceof OperatorToken) {
            TokenNode node = new TokenNode(token);
            if (((OperatorToken) cur.getToken()).getPriority() < ((OperatorToken) token).getPriority()) {
                node.setLeft(cur.getRight());
                cur.setRight(node);
                stack.push(cur);
            } else {
                node.setLeft(cur);
            }
            stack.push(node);
            node.setRight(new TokenNode());
            cur = node.getRight();
        } else {
            cur.setToken(token);
            stack.push(cur);
            cur.setRight(new TokenNode());
            cur = cur.getRight();
        }
        return cur;
    }
}

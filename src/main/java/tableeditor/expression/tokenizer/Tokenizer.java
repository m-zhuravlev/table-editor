package tableeditor.expression.tokenizer;

import tableeditor.expression.dso.DSO;
import tableeditor.expression.dso.NamedOperation;
import tableeditor.expression.exception.ExpressionException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public static List<Token> generateTokens(String input) throws ExpressionException {
        Pattern pattern = Pattern.compile("\\d\\.?\\s+(\\d|[a-zA-Z])");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            throw new ExpressionException("1: Invalid syntax from " + matcher.start() + " to " + matcher.end());
        }
        input = input.replaceAll(" ", "");

        int brkCount = 0, depth = 0;
        List<Token> tokens = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (Character.isDigit(ch)) {
                i = readNumber(i, input, tokens);
            } else if (DSO.mathOp.containsKey(String.valueOf(ch))) {
                tokens.add(new MathOperatorToken(DSO.mathOp.get(String.valueOf(ch))));
            } else if (DSO.compareOp.keySet().stream().anyMatch(k -> k.startsWith(String.valueOf(ch)))) {
                i = readCompareOperation(i, input, tokens);
            } else if (ch == '(') {
                tokens.add(new BracketOpenToken());
                brkCount++;
                if (depth > 0) depth++;
            } else if (ch == ')') {
                tokens.add(new BracketCloseToken());
                brkCount--;
                if (depth > 0) depth--;
            } else if (Character.isAlphabetic(ch)) {
                i = readFunctionOrCellLink(i, input, tokens);
                if (input.charAt(i) == '(') {
                    tokens.add(new BracketOpenToken());
                    depth++;
                    brkCount++;
                }
            } else if (ch == ',' && depth > 0) {
                tokens.add(new CommaToken());
            } else throw new ExpressionException("2: Invalid syntax character: '" + ch + "' at position " + i);
        }
        if (brkCount != 0) {
            throw new ExpressionException("3: Bracket count not correct ");
        }
        return tokens;
    }

    private static int readCompareOperation(int i, String input, List<Token> tokens) throws ExpressionException {
        StringBuilder sb = new StringBuilder();
        char ch = input.charAt(i);
        sb.append(ch);
        if (input.charAt(i + 1) == '=') {
            sb.append('=');
            i++;
        } else if (ch == '!' || ch == '=') {
            throw new ExpressionException("Not defined operator '" + ch + "'");
        }
        tokens.add(new CompareToken(DSO.compareOp.get(sb.toString())));
        return i;
    }

    private static int readFunctionOrCellLink(int ind, String input, List<Token> tokens) throws ExpressionException {
        StringBuilder sb = new StringBuilder();
        char ch = input.charAt(ind);
        sb.append(ch);
        for (ind++; ind < input.length(); ind++) {
            ch = input.charAt(ind);
            if (Character.isAlphabetic(ch) || Character.isDigit(ch)) {
                sb.append(ch);
            } else break;
        }
        String name = sb.toString();
        if (ch == '(') {
            NamedOperation operation = DSO.namedOp.get(name);
            if (operation == null) throw new ExpressionException("4: Function '" + name + "()' not found");
            tokens.add(new NamedFunctionToken(operation));
            return ind;
        } else {
            CellLinkToken token = makeCellLinkToken(name);
            if (token == null) throw new ExpressionException("5: Invalid cell link name '" + name + "' ");
            tokens.add(token);
            return ind - 1;
        }
    }

    private static CellLinkToken makeCellLinkToken(String name) {
        StringBuilder columnName = new StringBuilder();
        int i = 0;
        for (; i < name.length() && Character.isAlphabetic(name.charAt(i)); i++) {
            columnName.append(name.charAt(i));
        }
        StringBuilder rowNum = new StringBuilder();
        for (; i < name.length() && Character.isDigit(name.charAt(i)); i++) {
            rowNum.append(name.charAt(i));
        }
        if (i < name.length() || columnName.length() == 0 || rowNum.length() == 0) {
            return null;
        }
        return new CellLinkToken(columnName.toString(), rowNum.toString());
    }

    private static int readNumber(int ind, String input, List<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        boolean isContainsPoint = false;
        while (ind < input.length()) {
            char ch = input.charAt(ind);
            if (Character.isDigit(ch)) {
                sb.append(ch);
            } else if (ch == '.' && !isContainsPoint) {
                isContainsPoint = true;
                sb.append(ch);
            } else {
                break;
            }
            ind++;
        }
        tokens.add(new NumberToken(sb.toString()));
        return ind - 1;
    }
}

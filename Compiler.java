import java.util.ArrayList;
import java.util.List;

public class Compiler {

    public List<Token> lex(String input, int position) {

        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (Character.isDigit(currentChar)) {
                String number = readNumber(input, position);
                tokens.add(new Token(TokenType.NUMBER, number));
                position += number.length();
            } else if (isOperator(currentChar)) {
                tokens.add(new Token(getOperatorType(currentChar), String.valueOf(currentChar)));
                position++;
            } else if (currentChar == '(' || currentChar == ')') {
                tokens.add(new Token(TokenType.PAREN, String.valueOf(currentChar)));
                position++;
            } else if (Character.isWhitespace(currentChar)) {
                position++;
            } else if (currentChar == '\n') {
                break;
            } else {
                throw new RuntimeException("Unexpected character: " + currentChar);
            }
        }
        // position = 0;
        return tokens;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static TokenType getOperatorType(char c) {
        switch (c) {
            case '+':
                return TokenType.PLUS;
            case '-':
                return TokenType.MINUS;
            case '*':
                return TokenType.MULTIPLY;
            case '/':
                return TokenType.DIVIDE;
            default:
                throw new RuntimeException("Unknown operator: " + c);
        }
    }

    public ASTNode buildAST(String input, int position) {
        return Expr(input, position);
    }

    private static ASTNode Expr(String input, int position) {
        ASTNode termNode = Term(input, position);
        for (position = 1; position < input.length(); position++) {

            while (position < input.length() && (peek(input, position) == '+' || peek(input, position) == '-')) {
                char operator = peek(input, position);
                position++;
                ASTNode nextTermNode = Term(input, position);
                termNode = new ASTNode(String.valueOf(operator), termNode, nextTermNode);
            }
        }
        return termNode;
    }

    private static ASTNode Term(String input, int position) {
        ASTNode factorNode = Factor(input, position);

        while (position < input.length() && (peek(input, position) == '*' || peek(input, position) == '/')) {
            char operator = peek(input, position);

            position++;

            ASTNode nextFactorNode = Factor(input, position);
            factorNode = new ASTNode(String.valueOf(operator), factorNode, nextFactorNode);
        }

        return factorNode;
    }

    private static ASTNode Factor(String input, int position) {

        if (Character.isDigit(peek(input, position))) {

            return Number(input, position);
        } else if (peek(input, position) == '(') {
            position++;
            ASTNode expressionNode = Expr(input, position);
            if (peek(input, position) == ')') {
                position++;
                return expressionNode;
            } else {
                throw new RuntimeException("Expected closing parenthesis ')'");
            }
        } else {
            throw new RuntimeException("Unexpected character: " + peek(input, position));
        }
    }

    private static ASTNode Number(String input, int position) {

        String value = readNumber(input, position);
        return new ASTNode(value);
    }

    private static String readNumber(String input, int position) {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        return number.toString();
    }

    private static char peek(String input, int position) {
        if (position < input.length()) {
            return input.charAt(position);
        } else {
            return '\0'; // Null character if position is at the end of the input
        }
    }

    public String buildSyntaxAnalysis(ASTNode node) {
        StringBuilder result = new StringBuilder();
        buildSyntaxAnalysisRecursive(node, result);
        return result.toString();
    }

    private static void buildSyntaxAnalysisRecursive(ASTNode node, StringBuilder result) {
        if (node == null) {
            return;
        }

        if (node.getValue() != null) {
            result.append(node.getValue());
        } else {
            result.append("(");
            for (ASTNode child : node.getChildren()) {
                buildSyntaxAnalysisRecursive(child, result);
            }
            result.append(")");
        }
    }

    public void printAST(ASTNode node, int level) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < level; i++) {
            System.out.print("  "); // Adjust the spacing for better visualization
        }

        System.out.println(node);

        for (ASTNode child : node.getChildren()) {
            printAST(child, level + 1);
        }
    }
}

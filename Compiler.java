import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Compiler {
    private static String input;
    private static int position;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a mathematical expression (Q to quit): ");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("Q")) {
                break;
            }

            position = 0;

            try {
                List<Token> tokens = lex();
                System.out.println("Token Analysis:");
                for (Token token : tokens) {
                    System.out.println(token);
                }

                ASTNode root = buildAST();
                System.out.println("Syntax Analysis Result: " + buildSyntaxAnalysis(root));
                System.out.println("Abstract Syntax Tree:");
                printAST(root, 0);
            } catch (Exception e) {
                System.out.println("Expression is invalid. " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static List<Token> lex() {
        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (Character.isDigit(currentChar)) {
                tokens.add(new Token(TokenType.NUMBER, readNumber()));
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
        position = 0;
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

    private static ASTNode buildAST() {
        return Expr();
    }

    private static ASTNode Expr() {
        ASTNode termNode = Term();
        while (position < input.length() && (peek() == '+' || peek() == '-')) {
            char operator = peek();
            position++;
            ASTNode nextTermNode = Term();
            termNode = new ASTNode(String.valueOf(operator), termNode, nextTermNode);
        }
        return termNode;
    }

    private static ASTNode Term() {
        ASTNode factorNode = Factor();

        while (position < input.length() && (peek() == '*' || peek() == '/')) {
            char operator = peek();
            position++;
            ASTNode nextFactorNode = Factor();
            factorNode = new ASTNode(String.valueOf(operator), factorNode, nextFactorNode);
        }
        return factorNode;
    }

    private static ASTNode Factor() {
        if (Character.isDigit(peek())) {
            return Number();
        } else if (peek() == '(') {
            position++;
            ASTNode expressionNode = Expr();
            if (peek() == ')') {
                position++;
                return expressionNode;
            } else {
                throw new RuntimeException("Expected closing parenthesis ')'");
            }
        } else {
            throw new RuntimeException("Unexpected character: " + peek());
        }
    }

    private static ASTNode Number() {
        String value = readNumber();
        return new ASTNode(value);
    }

    private static String readNumber() {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        return number.toString();
    }

    private static char peek() {
        if (position < input.length()) {
            return input.charAt(position);
        } else {
            return '\0'; // Null character if position is at the end of the input
        }
    }

    private static String buildSyntaxAnalysis(ASTNode node) {
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

    private static void printAST(ASTNode node, int level) {
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

enum TokenType {
    NUMBER,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    PAREN
}

class Token {
    private final TokenType type;
    private final String lexeme;

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return type + ": " + lexeme;
    }
}

class ASTNode {
    private String value;
    private List<ASTNode> children;

    public ASTNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public ASTNode(String value, ASTNode left, ASTNode right) {
        this.value = value;
        this.children = new ArrayList<>();
        this.children.add(left);
        this.children.add(right);
    }

    public String getValue() {
        return value;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return (value != null) ? value : " ";
    }
}

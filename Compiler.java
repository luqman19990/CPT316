import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

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

class Main {

    static List<String> Tokenization(String input, List<String> v) {
        StringBuilder token = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (Character.isWhitespace(ch)) {
                // Skip whitespaces
                continue;
            } else if (isOperator(String.valueOf(ch)) || isParenthesis(String.valueOf(ch))) {
                // Add operators and parentheses as separate tokens
                if (token.length() > 0) {
                    v.add(token.toString());
                    token.setLength(0); // Reset the token
                }

                // Check for negative numbers
                if (ch == '-' && (i == 0 || isOperator(String.valueOf(input.charAt(i - 1)))
                        || isParenthesis(String.valueOf(input.charAt(i - 1))))) {
                    token.append(ch); // Include '-' in the token for negative numbers
                } else {
                    v.add(String.valueOf(ch));
                }
            } else {
                // Continue building the value token
                token.append(ch);
            }
        }

        // Add the last token if any
        if (token.length() > 0) {
            v.add(token.toString());
        }

        return v;
    }

    // Function to check if the current token is a ( or )
    static boolean isParenthesis(String ch) {
        return ch.equals("(") || ch.equals(")");
    }

    // check if the given string is an operator or not
    static boolean isOperator(String ch) {
        if (ch.length() > 1)
            return false;
        return ch.equals("+") || ch.equals("-") || ch.equals("*") || ch.equals("/");
    }

    // check if the given substring is a number/decimal number or not
    static boolean isNumber(String str) {
        int len = str.length();
        if (len == 0) {
            return false;
        }

        int i = 0;

        // Allow a '-' sign at the beginning
        if (str.charAt(0) == '-') {
            i = 1;
            // Check if there is only a '-' sign
            if (len == 1) {
                return false;
            }
        }
        int numOfDecimal = 0;
        for (; i < len; i++) {
            if (str.charAt(i) == '.') {
                numOfDecimal++;
            } else if (str.charAt(i) > '9' || str.charAt(i) < '0') {
                return false;
            }

            if ((str.charAt(i) > '9' || str.charAt(i) < '0') && str.charAt(i) != '.') {
                return false;
            }
        }

        if (numOfDecimal > 1 || str.charAt(0) == '.' || str.charAt(len - 1) == '.') {
            return false;
        }

        return true;
    }

    // Function to parse an expression based on the grammar rules
    static boolean expr(List<String> tokens, Iterator<String> it) {
        if (!it.hasNext())
            return false; // End of tokens

        // expr -> - expr
        if (it.next().equals("-") && isOperator(it.next())) {
            if (it.next().equals("-"))
                return false;
            return expr(tokens, it);
        }

        // expr -> ( expr )
        if (it.next().equals("(")) {
            if (!expr(tokens, it))
                return false;
            if (!it.hasNext() || !it.next().equals(")"))
                return false; // Missing )
            if (it.hasNext() && isOperator(it.next())) {
                // expr -> expr op expr
                it.next(); // Consume the operator
                return expr(tokens, it);
            }
            return true;
        }

        // expr -> id
        if (isNumber(it.next())) {
            if (it.next().equals("-") && isOperator(it.next())) {
                if (it.next().equals("-"))
                    return false;
                return expr(tokens, it);
            }

            if (it.hasNext() && isOperator(it.next())) {
                // expr -> expr op expr
                it.next(); // Consume the operator
                return expr(tokens, it);
            } else {
                return true; // No more tokens or next token is not an operator
            }
        }

        return false; // Token is not an identifier or a valid expression
    }

    static void parseExpressions(List<String> tokens, Iterator<String> it) {
        System.out.println("Parsing:");

        Stack<String> operators = new Stack<>();
        Stack<String> operands = new Stack<>();

        while (it.hasNext()) {
            String currentToken = it.next();

            if (isNumber(currentToken)) {
                operands.push(currentToken);
            } else if (isOperator(currentToken)) {
                while (!operators.isEmpty() && hasHigherPrecedence(operators.peek(), currentToken)) {
                    String operator = operators.pop();
                    if (operands.size() < 2) {
                        System.out.println("Invalid expression");
                        System.exit(1);
                    }
                    buildExpression(operator, operands);
                }
                operators.push(currentToken);
            } else {
                System.out.println("Invalid token: " + currentToken);
                return;
            }
        }

        while (!operators.isEmpty()) {
            if (operands.size() < 2) {
                System.out.println("Invalid expression");
                System.exit(1);
            }
            buildExpression(operators.pop(), operands);
        }

        if (operands.size() != 1) {
            System.out.println("Invalid expression");
            System.exit(1);
        }
        if (operands.size() == 1) {
            System.out.println("Parsed value:");
            System.out.println("{");
            System.out.println("\"type\": \"NumericLiteral\"");
            System.out.println("\"value\": " + operands.pop());
            System.out.println("}");
        }
    }

    // Function to build an expression with correct precedence
    static void buildExpression(String operator, Stack<String> operands) {
        System.out.println("Parsed value:");
        System.out.println("{");
        System.out.println("\"type\": \"BinaryExpression\",");
        System.out.println("\"op\": \"" + operator + "\",");
        System.out.println("\"left\": {");
        System.out.println("\"type\": \"NumericLiteral\",");
        System.out.println("\"value\": " + operands.pop());
        System.out.println("},");
        System.out.println("\"right\": {");
        System.out.println("\"type\": \"NumericLiteral\",");
        System.out.println("\"value\": " + operands.pop());
        System.out.println("}");

        operands.push(operator);
    }

    static boolean hasHigherPrecedence(String op1, String op2) {
        int precedenceOp1 = getPrecedence(op1);
        int precedenceOp2 = getPrecedence(op2);

        return precedenceOp1 > precedenceOp2;
    }

    static int getPrecedence(String operator) {
        switch (operator) {
            case "+":
                return 2;
            case "-":
                return 1;
            case "*":
                return 4;
            case "/":
                return 3;
            default:
                return 0; // Default precedence for unknown operators
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();

        String value;
        List<String> tokens = new ArrayList<>();

        while (true) {
            System.out.println("*\"E\" to terminate the program*");
            System.out.print("Enter a String: ");
            value = scanner.nextLine();

            if (value.equals("E")) {
                break;
            }

            tokens.clear();

            Tokenization(value, tokens);
            for (String token : tokens) {
                System.out.print(token);

                if (isNumber(token))
                    System.out.println(" Is a Value");
                else if (isOperator(token))
                    System.out.println(" Is an Operator");
                else if (isParenthesis(token))
                    System.out.println(" Is a Parenthesis");
                else
                    System.out.println(" Not accepted");
            }

            Iterator<String> it = tokens.iterator();
            parseExpressions(tokens, it);

            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            clearScreen();
        }

        scanner.close();
        return;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J"); // ANSI escape sequence to clear the screen
        System.out.flush();
    }
}

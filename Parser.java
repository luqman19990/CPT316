// Class representing a parser for mathematical expressions, producing an Abstract Syntax Tree (AST)
public class Parser {
    private String input; // The input string containing the mathematical expression
    private int position; // Current position while parsing the input

    // Constructor to initialize the Parser with the input string
    public Parser(String input) {
        this.input = input;
        this.position = 0;
    }

    // Method to initiate the parsing process and build the AST
    public ASTNode parse() {
        position = 0;
        return buildAST();
    }

    // Entry point for building the AST, starting with the Expr() method
    private ASTNode buildAST() {
        return Expr();
    }

    // Method to handle the parsing of an expression
    private ASTNode Expr() {
        ASTNode termNode = Term();

        // Handle addition and subtraction operators
        while (position < input.length() && (peek() == '+' || peek() == '-')) {
            char operator = peek();
            position++;
            ASTNode nextTermNode = Term();
            termNode = new ASTNode(String.valueOf(operator), termNode, nextTermNode);
        }
        return termNode;
    }

    // Method to handle the parsing of a term
    private ASTNode Term() {
        ASTNode factorNode = Factor();

        // Handle multiplication and division operators
        while (position < input.length() && (peek() == '*' || peek() == '/')) {
            char operator = peek();
            position++;
            ASTNode nextFactorNode = Factor();
            factorNode = new ASTNode(String.valueOf(operator), factorNode, nextFactorNode);
        }
        return factorNode;
    }

    // Method to handle the parsing of a factor
    private ASTNode Factor() {
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

    // Method to handle the parsing of a numeric value
    private ASTNode Number() {
        String value = readNumber();
        return new ASTNode(value);
    }

    // Method to read a sequence of consecutive digits and return the corresponding
    // numeric value
    private String readNumber() {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        return number.toString();
    }

    // Method to peek at the current character without advancing the position
    private char peek() {
        if (position < input.length()) {
            return input.charAt(position);
        } else {
            return '\0'; // Null character if position is at the end of the input
        }
    }
}

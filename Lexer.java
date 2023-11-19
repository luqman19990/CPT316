import java.util.ArrayList;
import java.util.List;

// Class representing a lexer for tokenizing mathematical expressions
public class Lexer {
    private String input; // The input string to be tokenized
    private int position; // Current position while scanning the input

    // Constructor to initialize the Lexer with the input string
    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    // Method to tokenize the input string and return a list of tokens
    public List<Token> lex() {
        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);

            // Check if the current character is a digit, indicating the start of a number
            if (Character.isDigit(currentChar)) {
                tokens.add(new Token(TokenType.NUMBER, readNumber()));
            } else if (isOperator(currentChar)) { // Check if the current character is an operator
                tokens.add(new Token(getOperatorType(currentChar), String.valueOf(currentChar)));
                position++;
            } else if (currentChar == '(' || currentChar == ')') { // Check for parentheses
                tokens.add(new Token(TokenType.PAREN, String.valueOf(currentChar)));
                position++;
                // Skip whitespace characters (currently commented out)
                // } else if (Character.isWhitespace(currentChar)) {
                // position++;
            } else if (currentChar == '\n') { // Break the loop if a newline character is encountered
                break;
            } else {
                // If none of the expected characters is found, throw an exception
                throw new RuntimeException("Unexpected character: " + currentChar);
            }
        }
        position = 0; // Reset position for subsequent lexing
        return tokens;
    }

    // Check if the character is one of the supported operators
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Get the TokenType corresponding to the operator character
    private TokenType getOperatorType(char c) {
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
                // Throw an exception if an unknown operator is encountered
                throw new RuntimeException("Unknown operator: " + c);
        }
    }

    // Read a sequence of consecutive digits to form a number
    private String readNumber() {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        return number.toString();
    }
}

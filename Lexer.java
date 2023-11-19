import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int position;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    public List<Token> lex() {
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
                // } else if (Character.isWhitespace(currentChar)) {
                // position++;
            } else if (currentChar == '\n') {
                break;
            } else {
                throw new RuntimeException("Unexpected character: " + currentChar);
            }
        }
        position = 0;
        return tokens;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

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
                throw new RuntimeException("Unknown operator: " + c);
        }
    }

    private String readNumber() {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        return number.toString();
    }

}

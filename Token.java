// Class representing a token in the context of lexical analysis
public class Token {
    private final TokenType type; // The type of the token, indicating its category
    private final String lexeme; // The lexeme, representing the actual text of the token

    // Constructor to initialize a Token with a specific TokenType and lexeme
    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    // Override toString method to provide a string representation of the token
    @Override
    public String toString() {
        return type + ": " + lexeme;
    }
}

// Enumeration representing the possible types of tokens
enum TokenType {
    NUMBER, // Numeric value token
    PLUS, // Addition operator token
    MINUS, // Subtraction operator token
    MULTIPLY, // Multiplication operator token
    DIVIDE, // Division operator token
    PAREN // Parentheses token
}

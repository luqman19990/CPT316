
enum TokenType {
    NUMBER,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    PAREN
}

public class Token {
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

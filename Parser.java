
public class Parser {
    private String input;
    private int position;

    public Parser(String input) {
        this.input = input;
        this.position = 0;
    }

    public ASTNode parse() {
        position = 0;
        return buildAST();
    }

    private ASTNode buildAST() {
        return Expr();
    }

    private ASTNode Expr() {
        ASTNode termNode = Term();
        while (position < input.length() && (peek() == '+' || peek() == '-')) {
            char operator = peek();
            position++;
            ASTNode nextTermNode = Term();
            termNode = new ASTNode(String.valueOf(operator), termNode, nextTermNode);
        }
        return termNode;
    }

    private ASTNode Term() {
        ASTNode factorNode = Factor();

        while (position < input.length() && (peek() == '*' || peek() == '/')) {
            char operator = peek();
            position++;
            ASTNode nextFactorNode = Factor();
            factorNode = new ASTNode(String.valueOf(operator), factorNode, nextFactorNode);
        }
        return factorNode;
    }

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

    private ASTNode Number() {
        String value = readNumber();
        return new ASTNode(value);
    }

    private String readNumber() {
        StringBuilder number = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        return number.toString();
    }

    private char peek() {
        if (position < input.length()) {
            return input.charAt(position);
        } else {
            return '\0'; // Null character if position is at the end of the input
        }
    }
}

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

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

        System.out.println("Parsed value:");
        System.out.println("{");
        System.out.println("\"type\": \"NumericLiteral\",");
        System.out.println("\"value\": " + operands.pop());
        System.out.println("}");
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

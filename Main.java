import java.util.List;
import java.util.Scanner;

// Main class for running the mathematical expression parser and AST builder
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        clearScreen();
        while (true) {

            System.out.print("Enter a mathematical expression (Q to quit): ");
            String input = scanner.nextLine();
            clearScreen();

            if (input.equalsIgnoreCase("Q")) {
                break;
            }

            // Initialize lexer and parser
            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(input);

            try {
                // Lexical analysis
                List<Token> tokens = lexer.lex();
                System.out.println("Expression : " + input);
                System.out.println();
                System.out.println("Token Analysis:");
                for (Token token : tokens) {
                    System.out.println(token);
                }

                // Syntax analysis
                ASTNode root = parser.parse();
                System.out.println("Syntax Analysis Result: " + buildSyntaxAnalysis(root));
                System.out.println("Abstract Syntax Tree:");
                printAST(root, 0);
            } catch (Exception e) {
                System.out.println("Expression is invalid. " + e.getMessage());
            }
        }

        scanner.close();
    }

    // Helper method to build syntax analysis result as a string
    private static String buildSyntaxAnalysis(ASTNode node) {
        StringBuilder result = new StringBuilder();
        buildSyntaxAnalysisRecursive(node, result);
        return result.toString().trim(); // Trim to remove leading/trailing whitespaces
    }

    // Helper method to recursively build syntax analysis result
    private static void buildSyntaxAnalysisRecursive(ASTNode node, StringBuilder result) {
        if (node == null) {
            return;
        }

        if (node.getValue() != null) {
            result.append(node.getValue()).append(" ");
        }

        for (ASTNode child : node.getChildren()) {
            buildSyntaxAnalysisRecursive(child, result);
        }
    }

    // Helper method to print the Abstract Syntax Tree (AST)
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

    // Helper method to clear the console screen
    private static void clearScreen() {
        System.out.print("\033[H\033[2J"); // ANSI escape sequence to clear the screen
        System.out.flush();
    }
}

import java.util.List;
import java.util.Scanner;

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

            Lexer lexer = new Lexer(input);
            Parser parser = new Parser(input);

            try {
                List<Token> tokens = lexer.lex();
                System.out.println("Expression : " + input);
                System.out.println();
                System.out.println("Token Analysis:");
                for (Token token : tokens) {
                    System.out.println(token);
                }

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

    private static void clearScreen() {
        System.out.print("\033[H\033[2J"); // ANSI escape sequence to clear the screen
        System.out.flush();
    }
}

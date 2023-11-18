import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Compiler compiler = new Compiler();
        // ASTNode ast = new ASTNode();

        while (true) {
            System.out.print("Enter a mathematical expression (Q to quit): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("Q")) {
                break;
            }

            int position = 0;

            try {
                List<Token> tokens = compiler.lex(input, position);
                System.out.println("Token Analysis:");
                for (Token token : tokens) {
                    System.out.println(token);
                }
                ASTNode root = compiler.buildAST(input, position);

                System.out.println("Syntax Analysis Result: " + compiler.buildSyntaxAnalysis(root));
                System.out.println("Abstract Syntax Tree:");
                compiler.printAST(root, 0);
            } catch (Exception e) {
                System.out.println("Expression is invalid. " + e.getMessage());
            }
        }

        scanner.close();
    }
}

import java.util.ArrayList;
import java.util.List;

// Class representing a node in an Abstract Syntax Tree (AST)
public class ASTNode {
    private String value; // The value associated with the node
    private List<ASTNode> children; // List of child nodes

    // Default constructor initializes the node with null value and an empty list of
    // children
    public ASTNode() {
        this.value = null;
        this.children = new ArrayList<>();
    }

    // Constructor with a value parameter, initializes the node with the given value
    // and an empty list of children
    public ASTNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    // Constructor with a value, left, and right parameters, initializes the node
    // with the given value
    // and adds left and right nodes as children
    public ASTNode(String value, ASTNode left, ASTNode right) {
        this.value = value;
        this.children = new ArrayList<>();
        this.children.add(left);
        this.children.add(right);
    }

    // Getter method to retrieve the value of the node
    public String getValue() {
        return value;
    }

    // Getter method to retrieve the list of children nodes
    public List<ASTNode> getChildren() {
        return children;
    }

    // Override toString method to provide a string representation of the node
    @Override
    public String toString() {
        // If the value is not null, return the value; otherwise, return a space
        // character
        return (value != null) ? value : " ";
    }
}

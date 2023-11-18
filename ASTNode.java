import java.util.ArrayList;
import java.util.List;

public class ASTNode {

    private String value;
    private List<ASTNode> children;

    public ASTNode() {

    }

    public ASTNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public ASTNode(String value, ASTNode left, ASTNode right) {
        this.value = value;
        this.children = new ArrayList<>();
        this.children.add(left);
        this.children.add(right);
    }

    public String getValue() {
        return value;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return (value != null) ? value : " ";
    }
}

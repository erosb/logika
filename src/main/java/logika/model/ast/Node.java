package logika.model.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import logika.model.ast.visitor.NodeVisitor;

public class Node {

    private final List<Node> children;

    public Node(final List<Node> children) {
        this.children = Collections.unmodifiableList(Objects.requireNonNull(children, "children cannot be null"));
    }

    public <R> R accept(final NodeVisitor<R> visitor) {
        return null;
    }

    public List<Node> getChildren() {
        return children;
    }

}

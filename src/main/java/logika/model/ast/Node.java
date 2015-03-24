package logika.model.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import logika.model.ast.visitor.NodeVisitor;

public abstract class Node {

    private final List<Node> children;

    public Node(final List<Node> children) {
        this.children = Collections.unmodifiableList(Objects.requireNonNull(children, "children cannot be null"));
    }

    public abstract <R> R accept(final NodeVisitor<R> visitor);

    public List<Node> getChildren() {
        return children;
    }

    public boolean isTerminal() {
        return children.isEmpty();
    }

    private <R> R visitChildren(final NodeVisitor<R> visitor) {
        R rval = visitor.identity();
        for (Node child : children) {
            rval = visitor.accumulate(rval, child.accept(visitor));
        }
        return rval;
    }

}

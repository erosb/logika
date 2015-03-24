package logika.model.ast;

import java.util.List;
import java.util.Objects;

import logika.model.Type;

public abstract class TermNode extends Node {

    private final Type type;

    public TermNode(final Type type, final List<Node> children) {
        super(children);
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    public Type getType() {
        return type;
    }
}

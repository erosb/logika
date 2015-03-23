package logika.model.ast;

import java.util.Objects;

import logika.model.Type;

public class TermNode extends Node {

    private final Type type;

    public TermNode(final Type type) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    public Type getType() {
        return type;
    }
}

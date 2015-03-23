package logika.model;

import java.util.Objects;

public class Variable {

    private final String name;

    private final Type type;

    public Variable(final String name, final Type type) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Var " + name + "(type: " + type.getName() + ")";
    }

}

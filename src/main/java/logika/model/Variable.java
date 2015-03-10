package logika.model;

public class Variable {

    private final String name;

    private final Type type;

    public Variable(final String name, final Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

}

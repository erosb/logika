package logika.model;

import java.util.List;

public class Term {
    
    private final String name;
    
    private final List<Type> argTypes;
    
    private final Type type;
    
    public Term(String name, List<Type> argTypes, Type type) {
        this.name = name;
        this.argTypes = argTypes;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public List<Type> getArgTypes() {
        return argTypes;
    }

    public Type getType() {
        return type;
    }

}

package logika.model;

import java.util.List;

public class Predicate {
    
    private final String name;
    
    private final List<Type> argTypes;

    public Predicate(String name, List<Type> argTypes) {
        this.name = name;
        this.argTypes = argTypes;
    }
    
    public String getName() {
        return name;
    }

    public List<Type> getArgTypes() {
        return argTypes;
    }

}

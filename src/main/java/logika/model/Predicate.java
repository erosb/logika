package logika.model;

import java.util.Collections;
import java.util.List;

public class Predicate {
	
	public static final Predicate TRUE = new Predicate("true", Collections.emptyList());
	
	public static final Predicate FALSE = new Predicate("false", Collections.emptyList());
    
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

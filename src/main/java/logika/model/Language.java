package logika.model;

import java.util.Set;

public class Language {
    
    private final Set<Type> types;
    
    private final Set<Constant> constants;
    
    private final Set<Predicate> predicates;
    
    private final Set<Term> terms;

    public Language(Set<Type> types, Set<Constant> constants, Set<Predicate> predicates, Set<Term> terms) {
        this.types = types;
        this.constants = constants;
        this.predicates = predicates;
        this.terms = terms;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public Set<Constant> getConstants() {
        return constants;
    }

    public Set<Predicate> getPredicates() {
        return predicates;
    }
    
    public Set<Term> getTerms() {
        return terms;
    }

    public Predicate predicateByName(String name) {
        return predicates.stream()
                .filter((p) -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("predicate ["+name+"] not found"));
    }
    
}

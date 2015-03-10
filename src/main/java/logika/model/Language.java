package logika.model;

import java.util.Set;

public class Language {

    private final Set<Type> types;

    private final Set<Constant> constants;

    private final Set<Predicate> predicates;

    private final Set<Term> terms;

    public Language(final Set<Type> types, final Set<Constant> constants, final Set<Predicate> predicates,
            final Set<Term> terms) {
        this.types = types;
        this.constants = constants;
        this.predicates = predicates;
        this.terms = terms;
    }

    public Constant constantByName(final String name) {
        return constants.stream().filter((c) -> c.getName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("constant [" + name + "] not found"));
    }

    public boolean constantExists(final String name) {
        return constants.stream().filter((c) -> c.getName().equals(name)).count() == 1;
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

    public Set<Type> getTypes() {
        return types;
    }

    public Predicate predicateByName(final String name) {
        return predicates.stream()
                .filter((p) -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("predicate [" + name + "] not found"));
    }

    public boolean predicateExists(final String text) {
        return predicates.stream().filter((p) -> p.getName().equals(text)).count() == 1;
    }

    public Term termByName(final String termName) {
        return terms.stream()
                .filter((t) -> t.getName().equals(termName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("term " + termName + " not found"));
    }

    public boolean termExists(final String term) {
        return terms.stream().filter((t) -> t.getName().equals(term)).count() == 1;
    }

}

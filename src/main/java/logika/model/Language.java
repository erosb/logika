package logika.model;

import java.util.HashSet;
import java.util.Set;

public class Language implements SymbolTable {

    private final Set<Type> types;

    private final Set<Constant> constants;

    private final Set<Predicate> predicates;

    private final Set<Function> functions;

    public Language() {
        this(new HashSet<Type>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public Language(final Set<Type> types, final Set<Constant> constants, final Set<Predicate> predicates,
            final Set<Function> functions) {
        this.types = types;
        this.constants = constants;
        this.predicates = predicates;
        this.functions = functions;
    }

    @Override
    public Constant constantByName(final String name) {
        return constants.stream().filter((c) -> c.getName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("constant [" + name + "] not found"));
    }

    @Override
    public boolean constantExists(final String name) {
        return constants.stream().filter((c) -> c.getName().equals(name)).count() == 1;
    }

    @Override
    public Function functionByName(final String fnName) {
        return functions.stream()
                .filter((t) -> t.getName().equals(fnName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("function [" + fnName + "] not found"));
    }

    @Override
    public boolean functionExists(final String fnName) {
        return functions.stream().filter((t) -> t.getName().equals(fnName)).count() == 1;
    }

    public Set<Constant> getConstants() {
        return constants;
    }

    public Set<Function> getFunctions() {
        return functions;
    }

    public Set<Predicate> getPredicates() {
        return predicates;
    }

    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public boolean isReservedName(final String name) {
        return constantExists(name) || functionExists(name) || predicateExists(name);
    }

    @Override
    public Predicate predicateByName(final String name) {
        return predicates.stream()
                .filter((p) -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("predicate [" + name + "] not found"));
    }

    @Override
    public boolean predicateExists(final String text) {
        return predicates.stream().filter((p) -> p.getName().equals(text)).count() == 1;
    }

    @Override
    public Variable varByName(final String varName) {
        throw new IllegalArgumentException("variable [" + varName + "] not found");
    }

}

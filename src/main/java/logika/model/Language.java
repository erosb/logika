package logika.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import logika.parser.TokenType;

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
        this.types = Collections.unmodifiableSet(types);
        this.constants = Collections.unmodifiableSet(constants);
        this.predicates = Collections.unmodifiableSet(predicates);
        this.functions = Collections.unmodifiableSet(functions);
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
    public boolean isBinaryOperator(final TokenType tokenType) {
        return tokenType == TokenType.AND || tokenType == TokenType.OR || tokenType == TokenType.IMPL;
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

    public Type typeByName(final String typeName) {
        return types.stream().filter((t) -> t.getName().equals(typeName)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("type [" + typeName + "] not found"));
    }

    @Override
    public Variable varByName(final String varName) {
        throw new IllegalArgumentException("variable [" + varName + "] not found");
    }

    @Override
    public boolean varDeclared(final String varName) {
        return false;
    }

}

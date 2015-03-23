package logika.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ScopingSymbolTable extends Language {

    private final SymbolTable parentScope;

    private final Set<Variable> variables = new HashSet<Variable>();

    private final Set<String> declaredVarNames = new HashSet<>();

    public ScopingSymbolTable(final SymbolTable parentScope) {
        this.parentScope = parentScope;
    }

    @Override
    public Constant constantByName(final String name) {
        return parentScope.constantByName(name);
    }

    @Override
    public boolean constantExists(final String name) {
        return parentScope.constantExists(name);
    }

    public void declareVar(final String varName) {
        if (isReservedName(varName)) {
            throw new IllegalArgumentException("symbol [" + varName + "] is already defined");
        }
        declaredVarNames.add(varName);
    }

    @Override
    public Function functionByName(final String fnName) {
        return parentScope.functionByName(fnName);
    }

    @Override
    public boolean functionExists(final String fnName) {
        return parentScope.functionExists(fnName);
    }

    @Override
    public Set<Constant> getConstants() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Function> getFunctions() {
        throw new UnsupportedOperationException();
    }

    public SymbolTable getParentScope() {
        return parentScope;
    }

    @Override
    public Set<Predicate> getPredicates() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Type> getTypes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReservedName(final String name) {
        return parentScope.isReservedName(name);
    }

    private boolean isVarDefinedInScope(final Variable var) {
        return variables.stream().filter((v) -> v.getName().equals(var.getName())).findFirst().isPresent();
    }

    @Override
    public Predicate predicateByName(final String name) {
        return parentScope.predicateByName(name);
    }

    @Override
    public boolean predicateExists(final String text) {
        return parentScope.predicateExists(text);
    }

    public void registerVariable(final Variable var) {
        if (isReservedName(var.getName()) || isVarDefinedInScope(var)) {
            throw new IllegalArgumentException("symbol [" + var.getName() + "] is already defined");
        }
        variables.add(var);
    }

    public Variable setVarType(final String varName, final Type type) {
        Iterator<String> varIt = declaredVarNames.iterator();
        boolean found = false;
        while (varIt.hasNext()) {
            String candidate = varIt.next();
            if (candidate.equals(varName)) {
                varIt.remove();
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("variable [" + varName + "] not found");
        }
        Variable rval = new Variable(varName, type);
        variables.add(rval);
        return rval;
    }

    @Override
    public Variable varByName(final String varName) {
        return variables.stream().filter((v) -> v.getName().equals(varName))
                .findFirst()
                .orElseGet(() -> parentScope.varByName(varName));
    }

    @Override
    public boolean varDeclared(final String varName) {
        return declaredVarNames.contains(varName);
    }

}

package logika.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ScopingSymbolTable extends Language {

    private final SymbolTable parentScope;

    private final Set<Variable> variables = new HashSet<Variable>();

    public ScopingSymbolTable(final SymbolTable parentScope) {
        this.parentScope = parentScope;
    }

    public SymbolTable getParentScope() {
        return parentScope;
    }

    @Override
    public boolean isReservedName(final String name) {
        return super.isReservedName(name)
                || variables.stream()
                .filter((v) -> v.getName().equals(name))
                .findFirst().isPresent();
    }

    public void registerVariable(final Variable var) {
        if (isReservedName(var.getName())) {
            throw new IllegalArgumentException("variable [" + var.getName() + "] is already defined");
        }
        variables.add(var);
    }

    public void setVarType(final String varName, final Type type) {
        Iterator<Variable> varIt = variables.iterator();
        boolean found = false;
        while (varIt.hasNext()) {
            Variable var = varIt.next();
            if (var.getName().equals(varName)) {
                if (var.getType() != null) {
                    throw new IllegalStateException("the type of variable [" + varName + "] is already set to "
                            + var.getType());
                }
                varIt.remove();
                found = true;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("variable [" + varName + "] not found");
        }
        variables.add(new Variable(varName, type));
    }

    @Override
    public Variable varByName(final String varName) {
        return variables.stream().filter((v) -> v.getName().equals(varName))
                .findFirst()
                .orElseGet(() -> parentScope.varByName(varName));
    }

}

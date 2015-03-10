package logika.model;

import java.util.HashSet;
import java.util.Set;

public class ScopingSymbolTable extends Language {

    private final SymbolTable parentScope;

    private final Set<Variable> variables = new HashSet<Variable>();

    public ScopingSymbolTable(final SymbolTable parentScope) {
        this.parentScope = parentScope;
    }

    public void registerVariable(final Variable var) {
        variables.add(var);
    }

    @Override
    public Variable varByName(final String varName) {
        return variables.stream().filter((v) -> v.getName().equals(varName))
                .findFirst()
                .orElseGet(() -> parentScope.varByName(varName));
    }

}

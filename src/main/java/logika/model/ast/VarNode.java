package logika.model.ast;

import java.util.Objects;

import logika.model.Variable;

public class VarNode extends TermNode {

    private final Variable var;

    public VarNode(final Variable var) {
        super(Objects.requireNonNull(var, "var cannot be null").getType());
        this.var = var;
    }

    public Variable getVar() {
        return var;
    }

}

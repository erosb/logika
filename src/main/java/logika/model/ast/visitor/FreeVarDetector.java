package logika.model.ast.visitor;

import java.util.Objects;

import logika.model.ast.Node;
import logika.model.ast.QuantifierNode;
import logika.model.ast.VarNode;

public class FreeVarDetector extends NodeVisitorBase<Boolean> {

    public static boolean hasFreeVar(final Node node, final String varName) {
        return node.accept(new FreeVarDetector(varName));
    }

    private final String varName;

    private FreeVarDetector(final String varName) {
        this.varName = Objects.requireNonNull(varName, "varName cannot be null");
    }

    @Override
    public Boolean accumulate(final Boolean previous, final Boolean current) {
        return previous || current;
    }

    public boolean hasFreeVar(final Node node) {
        Boolean rval = node.accept(this);
        return rval == null ? false : rval;
    }

    @Override
    public Boolean identity() {
        return false;
    }

    @Override
    public Boolean visitQuantifier(final QuantifierNode node) {
        if (node.getQuantifiedVarName().equals(varName)) {
            return false;
        }
        return super.visitQuantifier(node);
    }

    @Override
    public Boolean visitVar(final VarNode node) {
        return node.getVar().getName().equals(varName);
    }

}

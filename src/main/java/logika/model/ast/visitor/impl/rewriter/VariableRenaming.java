package logika.model.ast.visitor.impl.rewriter;

import java.util.Objects;

import logika.model.Variable;
import logika.model.ast.FormulaNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.VarNode;

public class VariableRenaming extends TreeRewriterBase {

    public static FormulaNode rename(final FormulaNode formula, final String renameFrom, final String renameTo) {
        return new VariableRenaming(renameFrom, renameTo).visitFormula(formula);
    }

    private final String renameFrom;

    private final String renameTo;

    public VariableRenaming(final String renameFrom, final String renameTo) {
        this.renameFrom = Objects.requireNonNull(renameFrom, "renameFrom cannot be null");
        this.renameTo = Objects.requireNonNull(renameTo, "renameTo cannot be null");
    }

    @Override
    public QuantifierNode visitQuantifier(final QuantifierNode node) {
        String quantifVar = node.getQuantifiedVarName();
        if (quantifVar.equals(renameFrom) || quantifVar.equals(renameTo)) {
            return node;
        }
        return super.visitQuantifier(node);
    }

    @Override
    public VarNode visitVar(final VarNode node) {
        if (node.getVar().getName().equals(renameFrom)) {
            return new VarNode(new Variable(renameTo, node.getType()));
        }
        return node;
    }

}

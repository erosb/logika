package logika.model.ast.visitor.impl;

import java.util.HashSet;
import java.util.Set;

import logika.model.ast.FormulaNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.VarNode;
import logika.model.ast.visitor.NodeVisitorBase;

public class FreeVarCollector extends NodeVisitorBase<Void> {

    public static final Set<String> collect(final FormulaNode input) {
        FreeVarCollector collector = new FreeVarCollector();
        input.accept(collector);
        return collector.freeVars;
    }

    private final Set<String> freeVars = new HashSet<>();

    private final Set<String> quantifiedVars = new HashSet<>();

    private FreeVarCollector() {
    }

    @Override
    public Void visitQuantifier(final QuantifierNode node) {
        quantifiedVars.add(node.getQuantifiedVarName());
        super.visitQuantifier(node);
        quantifiedVars.remove(node.getQuantifiedVarName());
        return null;
    }

    @Override
    public Void visitVar(final VarNode node) {
        String varName = node.getVar().getName();
        if (!quantifiedVars.contains(varName)) {
            freeVars.add(varName);
        }
        return super.visitVar(node);
    }

}

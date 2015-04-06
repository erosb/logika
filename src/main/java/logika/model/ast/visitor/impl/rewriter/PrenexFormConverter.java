package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.FormulaNode;

public class PrenexFormConverter {

    public static FormulaNode convert(final FormulaNode input) {
        FormulaNode actual = ImplicationEliminator.eliminate(input);
        actual = DuplicateNegationEliminator.eliminate(actual);
        actual = DeMorgan.negationsDown(actual);
        actual = CleanVarConverter.clean(actual);
        actual = QuantifierRemover.moveQuantifiersUp(actual);
        return actual;
    }

}

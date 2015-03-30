package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.FormulaNode;
import logika.model.ast.UnaryOpNode;
import logika.parser.TokenType;

public class DuplicateNegationEliminator extends TreeRewriterBase {

    public static final FormulaNode eliminate(final FormulaNode input) {
        return new DuplicateNegationEliminator().visitFormula(input);
    }

    private DuplicateNegationEliminator() {

    }

    @Override
    public FormulaNode visitUnaryOperator(final UnaryOpNode node) {
        if (node.getSubformula().is(TokenType.NOT)) {
            return visitFormula(node.getSubformula().getSubformula(0));
        } else {
            return node;
        }
    }
}

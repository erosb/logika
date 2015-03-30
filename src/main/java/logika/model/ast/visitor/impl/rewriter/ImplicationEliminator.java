package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.UnaryOpNode;
import logika.parser.TokenType;

public class ImplicationEliminator extends TreeRewriterBase {

    public static final FormulaNode eliminate(final FormulaNode input) {
        return new ImplicationEliminator().visitFormula(input);
    }

    @Override
    public FormulaNode visitBinaryOperator(final BinaryOpNode node) {
        if (node.is(TokenType.IMPL)) {
            FormulaNode resultLeft = visitFormula(node.getLeft()).negate();
            FormulaNode resultRight = visitFormula(node.getRight());
            return resultLeft.or(resultRight);
        }
        return super.visitBinaryOperator(node);
    }

    @Override
    public FormulaNode visitUnaryOperator(final UnaryOpNode node) {
        FormulaNode subformula = node.getSubformula();
        if (subformula.is(TokenType.IMPL)) {
            FormulaNode leftResult = visitFormula(subformula.getSubformula(0));
            FormulaNode rightResult = visitFormula(subformula.getSubformula(1)).negate();
            return leftResult.and(rightResult);
        }
        return super.visitUnaryOperator(node);
    }

}

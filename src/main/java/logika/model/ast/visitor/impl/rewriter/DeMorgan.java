package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.UnaryOpNode;
import logika.parser.Token;
import logika.parser.TokenType;

public class DeMorgan {

    private static class NegationsDown extends TreeRewriterBase {

        @Override
        public FormulaNode visitUnaryOperator(final UnaryOpNode node) {
            FormulaNode subformula = node.getSubformula();
            boolean isAnd = subformula.is(TokenType.AND);
            boolean isOr = subformula.is(TokenType.OR);
            boolean isAll = subformula.is(TokenType.ALL);
            boolean isAny = subformula.is(TokenType.ANY);
            if (isAnd || isOr) {
                FormulaNode resultLeft = visitFormula(subformula.getSubformula(0));
                FormulaNode resultRight = visitFormula(subformula.getSubformula(1));
                Token resultOp = isAnd ? Token.or() : Token.and();
                BinaryOpNode rval = new BinaryOpNode(resultOp,
                        visitFormula(resultLeft.negate()),
                        visitFormula(resultRight.negate()));
                return rval;
            } else if (isAll || isAny) {
                QuantifierNode qSub = (QuantifierNode) subformula;
                FormulaNode resultSub = visitFormula(subformula.getSubformula(0).negate());
                Token resultOp = isAll ? Token.any() : Token.all();
                QuantifierNode rval = new QuantifierNode(resultOp, qSub.getQuantifiedVar(), resultSub);
                return rval;
            } else {
                return DuplicateNegationEliminator.eliminate(node);
            }
        }

    }

    public static FormulaNode negationsDown(final FormulaNode input) {
        return new NegationsDown().visitFormula(input);
    }

}

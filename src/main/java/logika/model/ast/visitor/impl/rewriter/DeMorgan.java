package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
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
            if (isAnd || isOr) {
                FormulaNode andLeft = visitFormula(subformula.getSubformula(0));
                FormulaNode andRight = visitFormula(subformula.getSubformula(1));
                Token resultOp = isAnd ? Token.or() : Token.and();
                return new BinaryOpNode(resultOp, new UnaryOpNode(andLeft), new UnaryOpNode(andRight));
            }
            return super.visitUnaryOperator(node);
        }

    }

    public static FormulaNode negationsDown(final FormulaNode input) {
        return new NegationsDown().visitFormula(input);
    }

}

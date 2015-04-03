package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.QuantifierNode;
import logika.parser.Token;
import logika.parser.TokenType;

public class QuantifierRemover extends TreeRewriterBase {

    public static FormulaNode moveQuantifiersUp(final FormulaNode input) {
        return new QuantifierRemover().visitFormula(input);
    }

    private FormulaNode extractExistentialQuantifierFromDisjunction(final QuantifierNode left,
            QuantifierNode right) {
        right = toSameQuantifVarName(left, right);
        QuantifierNode result = new QuantifierNode(Token.any(), left.getQuantifiedVar(),
                new BinaryOpNode(Token.or(), left.getSubformula(), right.getSubformula()));
        return visitFormula(result);
    }

    private FormulaNode extractUniversalQuantifierFromConjunction(final QuantifierNode left, QuantifierNode right) {
        right = toSameQuantifVarName(left, right);
        QuantifierNode result = new QuantifierNode(Token.all(), left.getQuantifiedVar(),
                new BinaryOpNode(Token.and(), left.getSubformula(), right.getSubformula()));
        return visitFormula(result);
    }

    private QuantifierNode toSameQuantifVarName(final QuantifierNode left, final QuantifierNode right) {
        String leftQuantifVar = left.getQuantifiedVarName();
        String rightQuantifVar = right.getQuantifiedVarName();
        if (!leftQuantifVar.equals(rightQuantifVar)) {
            return new QuantifierNode(right.getToken(), left.getQuantifiedVar(),
                    VariableRenaming.rename(right.getSubformula(), rightQuantifVar, leftQuantifVar));
        }
        return right;
    }

    @Override
    public FormulaNode visitBinaryOperator(final BinaryOpNode node) {
        FormulaNode left = node.getLeft();
        FormulaNode right = node.getRight();
        if (node.is(TokenType.AND) && left.is(TokenType.ALL) && right.is(TokenType.ALL)) {
            return extractUniversalQuantifierFromConjunction((QuantifierNode) left, (QuantifierNode) right);
        } else if (node.is(TokenType.OR) && left.is(TokenType.ANY) && right.is(TokenType.ANY)) {
            return extractExistentialQuantifierFromDisjunction((QuantifierNode) left, (QuantifierNode) right);
        }
        return super.visitBinaryOperator(node);
    }
}

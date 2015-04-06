package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.visitor.impl.FreeVarDetector;
import logika.parser.Token;
import logika.parser.TokenType;

public class QuantifierRemover extends TreeRewriterBase {

    public static FormulaNode moveQuantifiersUp(final FormulaNode input) {
        QuantifierRemover remover = new QuantifierRemover();
        // FormulaNode prev, actual = input;
        // do {
        // prev = actual;
        // actual = remover.visitFormula(prev);
        // } while (actual != prev);
        return remover.visitFormula(input);
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

    private boolean isQuantifier(final FormulaNode left) {
        return left.is(TokenType.ANY) || left.is(TokenType.ALL);
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
    public FormulaNode visitBinaryOperator(BinaryOpNode node) {
        FormulaNode left = node.getLeft();
        FormulaNode right = node.getRight();
        boolean isAndOr = node.is(TokenType.AND) || node.is(TokenType.OR);
        if (node.is(TokenType.AND) && left.is(TokenType.ALL) && right.is(TokenType.ALL)) {
            return extractUniversalQuantifierFromConjunction((QuantifierNode) left, (QuantifierNode) right);
        } else if (node.is(TokenType.OR) && left.is(TokenType.ANY) && right.is(TokenType.ANY)) {
            return extractExistentialQuantifierFromDisjunction((QuantifierNode) left, (QuantifierNode) right);
        } else if (isAndOr) {
            FormulaNode rval;
            if (isQuantifier(left)) {
                QuantifierNode qLeft = (QuantifierNode) left;
                String quantifVar = qLeft.getQuantifiedVarName();
                if (FreeVarDetector.hasFreeVar(right, quantifVar)) {
                    node = (BinaryOpNode) CleanVarConverter.clean(node);
                    qLeft = (QuantifierNode) node.getLeft();
                    right = node.getRight();
                }
                qLeft = visitQuantifier(qLeft);
                right = visitFormula(right);
                rval = new QuantifierNode(qLeft.getToken(), qLeft.getQuantifiedVar(), new BinaryOpNode(node.getToken(),
                        qLeft.getSubformula(), right));
            } else if (isQuantifier(right)) {
                QuantifierNode qRight = (QuantifierNode) right;
                String quantifVar = qRight.getQuantifiedVarName();
                if (FreeVarDetector.hasFreeVar(left, quantifVar)) {
                    node = (BinaryOpNode) CleanVarConverter.clean(node);
                    qRight = (QuantifierNode) node.getRight();
                    left = node.getLeft();
                }
                qRight = visitQuantifier(qRight);
                left = visitFormula(left);
                rval = new QuantifierNode(qRight.getToken(), qRight.getQuantifiedVar(),
                        new BinaryOpNode(node.getToken(), left, qRight.getSubformula()));
            } else {
                return super.visitBinaryOperator(node);
            }
            return visitFormula(rval);
        }
        return super.visitBinaryOperator(node);
    }
}

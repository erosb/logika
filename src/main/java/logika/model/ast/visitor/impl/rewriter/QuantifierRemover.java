package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.VarNode;
import logika.parser.Token;
import logika.parser.TokenType;

public class QuantifierRemover extends TreeRewriterBase {

    public static FormulaNode moveQuantifiersUp(final FormulaNode input) {
        return new QuantifierRemover().visitFormula(input);
    }

    @Override
    public FormulaNode visitBinaryOperator(final BinaryOpNode node) {
        if (node.is(TokenType.AND)) {
            FormulaNode left = node.getLeft();
            FormulaNode right = node.getRight();
            if (left.is(TokenType.ALL) && right.is(TokenType.ALL)) {
                QuantifierNode qLeft = (QuantifierNode) left;
                QuantifierNode qRight = (QuantifierNode) right;
                String leftQuantifVar = qLeft.getQuantifiedVarName();
                String rightQuantifVar = qRight.getQuantifiedVarName();
                if (!leftQuantifVar.equals(rightQuantifVar)) {
                    qRight = new QuantifierNode(qRight.getToken(), qLeft.getQuantifiedVar(),
                            VariableRenaming.rename(qRight.getSubformula(), rightQuantifVar, leftQuantifVar));
                }
                QuantifierNode result = new QuantifierNode(Token.all(), (VarNode) left.getChildren().get(0),
                        new BinaryOpNode(Token.and(), qLeft.getSubformula(), qRight.getSubformula()));
                return visitFormula(result);
            }
        }
        return super.visitBinaryOperator(node);
    }
}

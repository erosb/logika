package logika.model.ast.visitor.impl.rewriter;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.UnaryOpNode;
import logika.parser.Token;
import logika.parser.TokenType;

public class ImplicationEliminator extends TreeRewriterBase {

    public static final FormulaNode eliminate(final FormulaNode input) {
        return new ImplicationEliminator().visitFormula(input);
    }

    @Override
    public FormulaNode visitBinaryOperator(final BinaryOpNode node) {
        if (node.is(TokenType.IMPL)) {
            FormulaNode resultLeft = new UnaryOpNode(node.getLeft());
            FormulaNode resultRight = node.getRight();
            return new BinaryOpNode(new Token(TokenType.OR, "or"), resultLeft, resultRight);
        }
        return super.visitBinaryOperator(node);
    }

}

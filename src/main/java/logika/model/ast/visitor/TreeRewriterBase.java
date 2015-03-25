package logika.model.ast.visitor;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.Node;

public class TreeRewriterBase extends NodeVisitorBase<Node> {

    @Override
    public BinaryOpNode visitBinaryOperator(final BinaryOpNode node) {
        return node;
    }

}

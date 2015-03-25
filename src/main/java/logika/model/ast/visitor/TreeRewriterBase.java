package logika.model.ast.visitor;

import logika.model.ast.Node;

public class TreeRewriterBase extends NodeVisitorBase<Node> {

    @Override
    protected Node visitChildren(final Node node) {
        return super.visitChildren(node);
    }

}

package logika.model.ast;

import java.util.List;

public abstract class FormulaNode extends Node {

    public FormulaNode(final List<Node> children) {
        super(children);
    }

}

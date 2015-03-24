package logika.model.ast;

import java.util.Collections;
import java.util.Objects;

import logika.model.Constant;
import logika.model.ast.visitor.NodeVisitor;

public class ConstantNode extends TermNode {

    private final Constant constant;

    public ConstantNode(final Constant constant) {
        super(Objects.requireNonNull(constant, "constant cannot be null").getType(), Collections.emptyList());
        this.constant = constant;
    }

    @Override
    public <R> R accept(final NodeVisitor<R> visitor) {
        return visitor.visitConstant(this);
    }

    public Constant getConstant() {
        return constant;
    }

}

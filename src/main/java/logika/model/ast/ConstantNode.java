package logika.model.ast;

import java.util.Collections;
import java.util.Objects;

import logika.model.Constant;

public class ConstantNode extends TermNode {

    private final Constant constant;

    public ConstantNode(final Constant constant) {
        super(Objects.requireNonNull(constant, "constant cannot be null").getType(), Collections.emptyList());
        this.constant = constant;
    }

    public Constant getConstant() {
        return constant;
    }

}

package logika.model.ast;

import java.util.Arrays;
import java.util.Objects;

import logika.model.ast.visitor.NodeVisitor;

public class UnaryOpNode extends FormulaNode {

    public static enum UnaryOperator {
        NOT
    }

    private final UnaryOperator operator = UnaryOperator.NOT;

    private final FormulaNode subformula;

    public UnaryOpNode(final FormulaNode subformula) {
        super(Arrays.asList(subformula));
        this.subformula = Objects.requireNonNull(subformula, "subformula cannot be null");
    }

    @Override
    public <R> R accept(final NodeVisitor<R> visitor) {
        return visitor.visitUnaryOperator(this);
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public FormulaNode getSubformula() {
        return subformula;
    }

}

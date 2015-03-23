package logika.model.ast;

import java.util.Objects;

public class UnaryOpNode extends FormulaNode {

    public static enum UnaryOperator {
        NOT
    }

    private final UnaryOperator operator = UnaryOperator.NOT;

    private final FormulaNode subformula;

    public UnaryOpNode(final FormulaNode subformula) {
        this.subformula = Objects.requireNonNull(subformula, "subformula cannot be null");
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public FormulaNode getSubformula() {
        return subformula;
    }

}

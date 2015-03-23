package logika.model.ast;

import java.util.Objects;

public class QuantifierNode extends FormulaNode {

    public static enum Quantifier {
        ALL, ANY
    }

    private final Quantifier quantifier;

    private final FormulaNode subformula;

    public QuantifierNode(final Quantifier quantifier, final FormulaNode subformula) {
        this.quantifier = Objects.requireNonNull(quantifier, "quantifier cannot be null");
        this.subformula = Objects.requireNonNull(subformula, "subformula cannot be null");
    }

    public Quantifier getQuantifier() {
        return quantifier;
    }

    public FormulaNode getSubformula() {
        return subformula;
    }

}

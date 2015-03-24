package logika.model.ast;

import java.util.Arrays;
import java.util.Objects;

import logika.model.Variable;
import logika.parser.TokenType;

public class QuantifierNode extends FormulaNode {

    private final Variable quantifiedVar;

    private final TokenType quantifier;

    private final FormulaNode subformula;

    public QuantifierNode(final TokenType quantifier, final Variable quantifiedVar, final FormulaNode subformula) {
        super(Arrays.asList(subformula));
        this.quantifier = Objects.requireNonNull(quantifier, "quantifier cannot be null");
        this.quantifiedVar = Objects.requireNonNull(quantifiedVar, "quantifiedVar cannot be null");
        this.subformula = Objects.requireNonNull(subformula, "subformula cannot be null");
    }

    public Variable getQuantifiedVar() {
        return quantifiedVar;
    }

    public TokenType getQuantifier() {
        return quantifier;
    }

    public FormulaNode getSubformula() {
        return subformula;
    }

}

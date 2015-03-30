package logika.model.ast;

import java.util.Arrays;
import java.util.Objects;

import logika.model.ast.visitor.NodeVisitor;
import logika.parser.Token;
import logika.parser.TokenType;

public class QuantifierNode extends FormulaNode {

    private final VarNode quantifiedVar;

    private final TokenType quantifier;

    private final FormulaNode subformula;

    public QuantifierNode(final Token token, final VarNode quantifiedVar,
            final FormulaNode subformula) {
        super(token, Arrays.asList(quantifiedVar, subformula));
        this.quantifier = token.getType();
        this.quantifiedVar = Objects.requireNonNull(quantifiedVar, "quantifiedVar cannot be null");
        this.subformula = Objects.requireNonNull(subformula, "subformula cannot be null");
    }

    @Override
    public <R> R accept(final NodeVisitor<R> visitor) {
        return visitor.visitQuantifier(this);
    }

    public VarNode getQuantifiedVar() {
        return quantifiedVar;
    }

    public String getQuantifiedVarName() {
        return quantifiedVar.getVar().getName();
    }

    public TokenType getQuantifier() {
        return quantifier;
    }

    public FormulaNode getSubformula() {
        return subformula;
    }

}

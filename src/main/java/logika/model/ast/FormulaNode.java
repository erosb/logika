package logika.model.ast;

import java.util.ArrayList;
import java.util.List;

import logika.parser.Token;
import logika.parser.TokenType;

public abstract class FormulaNode extends Node {

    private final List<FormulaNode> subformulas;

    public FormulaNode(final Token token, final List<Node> children) {
        super(token, children);
        subformulas = new ArrayList<FormulaNode>(2);
        for (Node n : children) {
            if (n instanceof FormulaNode) {
                subformulas.add((FormulaNode) n);
            }
        }
    }

    public BinaryOpNode and(final FormulaNode right) {
        return new BinaryOpNode(Token.and(), this, right);
    }

    public FormulaNode getSubformula(final int idx) {
        return subformulas.get(idx);
    }

    public List<FormulaNode> getSubformulas() {
        return subformulas;
    }

    public UnaryOpNode negate() {
        return new UnaryOpNode(this);
    }

    public BinaryOpNode or(final FormulaNode right) {
        return new BinaryOpNode(Token.or(), this, right);
    }

}

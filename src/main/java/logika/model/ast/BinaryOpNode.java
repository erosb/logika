package logika.model.ast;

import java.util.Arrays;
import java.util.Objects;

import logika.model.ast.visitor.NodeVisitor;
import logika.parser.TokenType;

public class BinaryOpNode extends FormulaNode {

    private final TokenType operator;

    private final FormulaNode left;

    private final FormulaNode right;

    public BinaryOpNode(final TokenType operator, final FormulaNode left, final FormulaNode right) {
        super(Arrays.asList(left, right));
        this.operator = Objects.requireNonNull(operator, "operator cannot be null");
        this.left = Objects.requireNonNull(left, "left cannot be null");
        this.right = Objects.requireNonNull(right, "right cannot be null");
    }

    @Override
    public <R> R accept(final NodeVisitor<R> visitor) {
        return visitor.visitBinaryOperator(this);
    }

    public FormulaNode getLeft() {
        return left;
    }

    public TokenType getOperator() {
        return operator;
    }

    public FormulaNode getRight() {
        return right;
    }

}

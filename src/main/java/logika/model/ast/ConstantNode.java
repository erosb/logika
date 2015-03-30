package logika.model.ast;

import java.util.Collections;
import java.util.Objects;

import logika.model.Constant;
import logika.model.ast.visitor.NodeVisitor;
import logika.parser.Token;
import logika.parser.TokenType;

public class ConstantNode extends TermNode {

    private final Constant constant;

    public ConstantNode(final Token token, final Constant constant) {
        super(new Token(TokenType.ID, Objects.requireNonNull(constant.getName(), "constant cannot be null")),
                constant.getType(),
                Collections.emptyList());
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

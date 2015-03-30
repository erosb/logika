package logika.model.ast;

import java.util.Collections;
import java.util.Objects;

import logika.model.Variable;
import logika.model.ast.visitor.NodeVisitor;
import logika.parser.Token;
import logika.parser.TokenType;

public class VarNode extends TermNode {

    private final Variable var;

    public VarNode(final Variable var) {
        super(new Token(TokenType.ID, var.getName()), Objects.requireNonNull(var, "var cannot be null").getType(),
                Collections.emptyList());
        this.var = var;
    }

    @Override
    public <R> R accept(final NodeVisitor<R> visitor) {
        return visitor.visitVar(this);
    }

    public Variable getVar() {
        return var;
    }

}

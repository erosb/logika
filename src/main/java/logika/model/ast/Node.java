package logika.model.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import logika.model.ast.visitor.NodeVisitor;
import logika.parser.Token;
import logika.parser.TokenType;

public abstract class Node {

    private final Token token;

    private final List<Node> children;

    public Node(final Token token, final List<Node> children) {
        this.token = Objects.requireNonNull(token, "token cannot be null");
        this.children = Collections.unmodifiableList(Objects.requireNonNull(children, "children cannot be null"));
    }

    public abstract <R> R accept(final NodeVisitor<R> visitor);

    public List<Node> getChildren() {
        return children;
    }

    public Token getToken() {
        return token;
    }
    
    public boolean is(TokenType tokenType) {
        return token.getType() == tokenType;
    }

    public boolean isTerminal() {
        return children.isEmpty();
    }

}

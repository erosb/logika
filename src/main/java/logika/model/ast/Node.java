package logika.model.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import logika.model.ast.visitor.NodeVisitor;
import logika.model.ast.visitor.impl.DefaultSerializerVisitor;
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        if (children == null) {
            if (other.children != null) {
                return false;
            }
        } else if (!children.equals(other.children)) {
            return false;
        }
        if (token == null) {
            if (other.token != null) {
                return false;
            }
        } else if (!token.equals(other.token)) {
            return false;
        }
        return true;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((token == null) ? 0 : token.hashCode());
        return result;
    }

    public boolean is(final TokenType tokenType) {
        return token.getType() == tokenType;
    }

    public boolean isTerminal() {
        return children.isEmpty();
    }

    @Override
    public String toString() {
        return accept(new DefaultSerializerVisitor());
    }

}

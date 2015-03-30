package logika.model.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import logika.model.Predicate;
import logika.model.ast.visitor.NodeVisitor;
import logika.parser.Token;
import logika.parser.TokenType;

public class PredicateNode extends FormulaNode {

    private final Predicate predicate;

    private final List<TermNode> arguments;

    public PredicateNode(final Predicate predicate, final List<TermNode> arguments) {
        super(new Token(TokenType.ID, predicate.getName()), new ArrayList<Node>(arguments));
        this.predicate = Objects.requireNonNull(predicate, "predicate cannot be null");
        this.arguments = Collections.unmodifiableList(Objects.requireNonNull(arguments, "arguments cannot be null"));
    }

    @Override
    public <R> R accept(final NodeVisitor<R> visitor) {
        return visitor.visitPredicate(this);
    }

    public TermNode getArgument(final int i) {
        return arguments.get(i);
    }

    public List<TermNode> getArguments() {
        return arguments;
    }

    public Predicate getPredicate() {
        return predicate;
    }

}

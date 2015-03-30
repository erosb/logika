package logika.model.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import logika.model.Function;
import logika.model.ast.visitor.NodeVisitor;
import logika.parser.Token;

public class FunctionNode extends TermNode {

    private final Function function;

    private final List<TermNode> arguments;

    public FunctionNode(final Token token, final Function function, final List<TermNode> arguments) {
        super(token, Objects.requireNonNull(function, "function cannot be null").getType(), new ArrayList<Node>(
                arguments));
        this.function = function;
        this.arguments = Collections.unmodifiableList(Objects.requireNonNull(arguments, "arguments cannot be null"));
    }

    @Override
    public <R> R accept(final NodeVisitor<R> visitor) {
        return visitor.visitFunction(this);
    }

    public TermNode getArg(final int i) {
        return arguments.get(i);
    }

    public List<TermNode> getArguments() {
        return arguments;
    }

    public Function getFunction() {
        return function;
    }

}

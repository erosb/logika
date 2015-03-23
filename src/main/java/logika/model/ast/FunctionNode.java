package logika.model.ast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import logika.model.Function;

public class FunctionNode extends TermNode {

    private final Function function;

    private final List<TermNode> arguments;

    public FunctionNode(final Function function, final List<TermNode> arguments) {
        super(Objects.requireNonNull(function, "function cannot be null").getType());
        this.function = function;
        this.arguments = Collections.unmodifiableList(Objects.requireNonNull(arguments, "arguments cannot be null"));
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

package logika.model.ast.visitor;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.ConstantNode;
import logika.model.ast.FunctionNode;
import logika.model.ast.Node;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.VarNode;

public class DefaultSerializerVisitor extends NodeVisitorBase<String> {

    @Override
    public String accumulate(final String previous, final String current) {
        if (previous == null) {
            return current;
        }
        return previous + ", " + current;
    }

    private void appendChildrenInParens(final Node node, final StringBuilder buffer) {
        buffer.append('(').append(visitChildren(node)).append(')');
    }

    public String serialize(final Node input) {
        return input.accept(this);
    }

    @Override
    public String visitBinaryOperator(final BinaryOpNode node) {
        StringBuilder rval = new StringBuilder(node.getOperator().name().toLowerCase());
        appendChildrenInParens(node, rval);
        return rval.toString();
    }

    @Override
    public String visitConstant(final ConstantNode node) {
        return node.getConstant().getName();
    }

    @Override
    public String visitFunction(final FunctionNode node) {
        StringBuilder rval = new StringBuilder(node.getFunction().getName());
        appendChildrenInParens(node, rval);
        return rval.toString();
    }

    @Override
    public String visitPredicate(final PredicateNode node) {
        String predName = node.getPredicate().getName();
        if (node.isTerminal()) {
            return predName;
        }
        StringBuilder rval = new StringBuilder(predName);
        appendChildrenInParens(node, rval);
        return rval.toString();
    }

    @Override
    public String visitQuantifier(final QuantifierNode node) {
        StringBuilder rval = new StringBuilder(node.getQuantifier().name().toLowerCase());
        appendChildrenInParens(node, rval);
        return rval.toString();
    }

    @Override
    public String visitUnaryOperator(final UnaryOpNode node) {
        StringBuilder rval = new StringBuilder(node.getOperator().name().toLowerCase());
        appendChildrenInParens(node, rval);
        return rval.toString();
    }

    @Override
    public String visitVar(final VarNode node) {
        return node.getVar().getName();
    }

}

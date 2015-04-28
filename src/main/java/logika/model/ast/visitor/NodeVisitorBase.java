package logika.model.ast.visitor;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.ConstantNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.FunctionNode;
import logika.model.ast.Node;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.TermNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.VarNode;

public class NodeVisitorBase<R> implements NodeVisitor<R> {

    public R accumulate(final R previous, final R current) {
        return current;
    }

    public R identity() {
        return null;
    }

    @Override
    public R visitBinaryOperator(final BinaryOpNode node) {
        return visitFormula(node);
    }

    protected R visitChildren(final Node node) {
        return node.getChildren().stream()
                .map((n) -> n.accept(this))
                .reduce(identity(), this::accumulate);
    }

    @Override
    public R visitConstant(final ConstantNode node) {
        return visitTerm(node);
    }

    @Override
    public R visitFormula(final FormulaNode node) {
        return visitChildren(node);
    }

    @Override
    public R visitFunction(final FunctionNode node) {
        return visitTerm(node);
    }

    @Override
    public R visitPredicate(final PredicateNode node) {
        return visitFormula(node);
    }

    @Override
    public R visitQuantifier(final QuantifierNode node) {
        return visitFormula(node);
    }

    @Override
    public R visitTerm(final TermNode node) {
        return visitChildren(node);
    }

    @Override
    public R visitUnaryOperator(final UnaryOpNode node) {
        return visitFormula(node);
    }

    @Override
    public R visitVar(final VarNode node) {
        return visitTerm(node);
    }

}

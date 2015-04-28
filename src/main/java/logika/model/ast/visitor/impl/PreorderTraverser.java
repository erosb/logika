package logika.model.ast.visitor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import logika.model.ast.FormulaNode;
import logika.model.ast.Node;
import logika.model.ast.visitor.NodeVisitorBase;
import logika.parser.Token;
import static java.util.Objects.requireNonNull;

public class PreorderTraverser extends NodeVisitorBase<Void> {

    private static class NodeListCollector implements Consumer<Node> {

        List<Node> nodeList = new ArrayList<>();

        @Override
        public void accept(final Node t) {
            nodeList.add(t);
        }

    }

    public static List<Node> toNodeList(final Node tree) {
        NodeListCollector collector = new NodeListCollector();
        traverse(tree, collector);
        return collector.nodeList;
    }

    public static void traverse(final Node input, final Consumer<Node> consumer) {
        input.accept(new PreorderTraverser(consumer));
    }

    private final Consumer<Node> consumer;

    public PreorderTraverser(final Consumer<Node> consumer) {
        this.consumer = requireNonNull(consumer, "consumer cannot be null");
    }

    @Override
    protected Void visitChildren(final Node node) {
        consumer.accept(node);
        return super.visitChildren(node);
    }

}

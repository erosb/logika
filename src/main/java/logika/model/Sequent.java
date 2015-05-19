package logika.model;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import logika.model.ast.BinaryOpNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.visitor.NodeVisitorBase;
import logika.parser.TokenType;

public class Sequent {

    private static class FormulaListBuilder extends NodeVisitorBase<List<FormulaNode>> {

        private TokenType separator;

        @Override
        public List<FormulaNode> accumulate(List<FormulaNode> previous, List<FormulaNode> current) {
            if (previous.isEmpty() && current.isEmpty()) {
                return Collections.emptyList();
            }
            if (previous.isEmpty()) {
                return current;
            }
            if (current.isEmpty()) {
                return previous;
            }
            List<FormulaNode> rval = new ArrayList<>(previous.size() + current.size());
            rval.addAll(previous);
            rval.addAll(current);
            return rval;
        }

        @Override
        public List<FormulaNode> identity() {
            return Collections.emptyList();
        }

        public FormulaListBuilder(TokenType separator) {
            this.separator = separator;
        }

        @Override
        public List<FormulaNode> visitBinaryOperator(BinaryOpNode node) {
            if (node.is(separator)) {
                Collection<FormulaNode> leftResult = node.getLeft().accept(this);
                Collection<FormulaNode> rightResult = node.getRight().accept(this);
                List<FormulaNode> rval = new ArrayList<>(leftResult.size() + rightResult.size());
                rval.addAll(leftResult);
                rval.addAll(rightResult);
                return rval;
            }
            return Arrays.asList(node);
        }

        @Override
        public List<FormulaNode> visitPredicate(PredicateNode node) {
            return Arrays.asList(node);
        }

        @Override
        public List<FormulaNode> visitQuantifier(QuantifierNode node) {
            return Arrays.asList(node);
        }

        @Override
        public List<FormulaNode> visitUnaryOperator(UnaryOpNode node) {
            return Arrays.asList(node);
        }

    }
    
    public static Sequent forFormula(FormulaNode input) {
        if (!input.is(TokenType.IMPL)) {
            throw new IllegalArgumentException(format(
                    "formula %s is not an implication", input.toString()));
        }
        BinaryOpNode implication = (BinaryOpNode) input;
        Collection<FormulaNode> premises = implication.getLeft().accept(new FormulaListBuilder(TokenType.AND));
        Collection<FormulaNode> conclusion = implication.getRight().accept(new FormulaListBuilder(TokenType.OR));
        return new Sequent(premises, conclusion);
    }

    private final Collection<FormulaNode> premises;

    private final Collection<FormulaNode> conclusions;

    public Sequent(Collection<FormulaNode> premises, Collection<FormulaNode> conclusions) {
        this.premises = Collections.unmodifiableCollection(premises);
        this.conclusions = Collections.unmodifiableCollection(conclusions);
    }

    public Collection<FormulaNode> premises() {
        return premises;
    }

    public Collection<FormulaNode> conclusions() {
        return conclusions;
    }
    
    
    public boolean isTerminal() {
        return premises.stream().filter(conclusions::contains).findFirst().isPresent();
    }
    
    @Override
    public String toString() {
        return premises.stream().map(FormulaNode::toString).collect(Collectors.joining(", "))
                + " -> "
                + conclusions.stream().map(FormulaNode::toString).collect(Collectors.joining(", "));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((conclusions == null) ? 0 : conclusions.hashCode());
        result = prime * result + ((premises == null) ? 0 : premises.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sequent other = (Sequent) obj;
        return collectionsEqual(premises, other.premises) && collectionsEqual(conclusions, other.conclusions);
    }

    private boolean collectionsEqual(Collection<FormulaNode> coll1, Collection<FormulaNode> coll2) {
        Iterator<FormulaNode> c1It = coll1.iterator();
        Iterator<FormulaNode> c2It = coll2.iterator();
        while (c1It.hasNext()) {
            if (!c2It.hasNext()) {
                return false;
            }
            FormulaNode c1Elem = c1It.next();
            FormulaNode c2Elem = c2It.next();
            if (!Objects.equals(c1Elem, c2Elem)) {
                return false;
            }
        }
        return (!c2It.hasNext());
    }
    
}

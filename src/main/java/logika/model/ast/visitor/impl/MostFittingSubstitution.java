package logika.model.ast.visitor.impl;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import logika.model.Language;
import logika.model.ast.FormulaNode;
import logika.model.ast.Node;
import logika.model.ast.TermNode;
import logika.model.ast.VarNode;
import logika.model.ast.visitor.NodeVisitorBase;

public class MostFittingSubstitution extends NodeVisitorBase<Node> {

    public static Optional<Node> determine(final Language lang, final List<Node> input) {
        List<Node> slaveList = input.subList(1, input.size());
        return new MostFittingSubstitution(lang, slaveList).getMostFittingSubstitution(input.get(0));
    }

    private final Language lang;

    private final List<Node> slaveList;

    public MostFittingSubstitution(final Language lang, final List<Node> slaveList) {
        this.lang = requireNonNull(lang, "lang cannot be null");
        this.slaveList = slaveList;
    }

    private Optional<Node> getMostFittingSubstitution(final Node master) {
        Node rval = master.accept(this);
        if (rval == null) {
            return Optional.empty();
        }
        return Optional.of(rval);
    }

    private boolean hasAnyFreeVars(final Node node, final Collection<VarNode> vars) {
        return vars.stream()
                .filter(var -> FreeVarDetector.hasFreeVar(node, var.getVar().getName()))
                .findAny().isPresent();
    }

    private TermNode lookupReplacementTerm(final Collection<TermNode> diff) {
        Collection<VarNode> vars = diff.stream()
                .filter(c -> c instanceof VarNode)
                .map(c -> (VarNode) c)
                .collect(Collectors.toList());
        return diff.stream()
                .filter(node -> !hasAnyFreeVars(node, vars))
                .findFirst().orElse(
                        diff.stream()
                        .filter(node -> vars.contains(node))
                        .findFirst().orElse(null)
                        );
    }

    @Override
    public Node visitFormula(final FormulaNode master) {
        if (slaveList.stream()
                .filter(slave -> slave.getChildren().isEmpty())
                .filter(slave -> slave.getToken().equals(master.getToken()))
                .count() == slaveList.size()) {
            return master;
        }
        if (slaveList.stream().filter(slave -> !slave.getToken().equals(master.getToken())).findAny().isPresent()) {
            return null;
        }
        List<Node> childrenOfMaster = master.getChildren();
        List<Node> newChildList = new ArrayList<>(master.getChildren().size());
        for (int i = 0; i < childrenOfMaster.size(); ++i) {
            List<Node> newInputList = new ArrayList<>();
            newInputList.add(childrenOfMaster.get(i));
            for (Node n : slaveList) {
                newInputList.add(n.getChildren().get(i));
            }
            Optional<Node> result = determine(lang, newInputList);
            if (result.isPresent()) {
                newChildList.add(result.get());
            } else {
                return null;
            }
        }
        return lang.forToken(master.getToken(), newChildList);
    }

    @Override
    public Node visitTerm(final TermNode master) {
        Collection<TermNode> diff = new ArrayList<>(slaveList.size() + 1);
        diff.add(master);
        for (Node slave : slaveList) {
            if (!slave.getToken().equals(master.getToken())) {
                diff.add((TermNode) slave);
            }
        }
        if (diff.size() == 1) {
            return master;
        }
        return lookupReplacementTerm(diff);
    }

}

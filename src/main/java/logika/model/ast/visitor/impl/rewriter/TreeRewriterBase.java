package logika.model.ast.visitor.impl.rewriter;

import java.util.ArrayList;
import java.util.List;

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
import logika.model.ast.visitor.NodeVisitorBase;

public class TreeRewriterBase extends NodeVisitorBase<Node> {

    private List<TermNode> visitArgList(final List<TermNode> args) {
        List<TermNode> argResults = new ArrayList<>(args.size());
        boolean resultIsDifferent = false;
        for (TermNode child : args) {
            TermNode result = visitTerm(child);
            argResults.add(result);
            if (argResults != child) {
                resultIsDifferent = true;
            }
        }
        if (resultIsDifferent) {
            return argResults;
        }
        return args;
    }

    @Override
    public BinaryOpNode visitBinaryOperator(final BinaryOpNode node) {
        FormulaNode leftOrig = node.getLeft();
        FormulaNode leftResult = this.visitFormula(leftOrig);
        FormulaNode rightOrig = node.getRight();
        FormulaNode rightResult = this.visitFormula(rightOrig);
        if (!(leftOrig == leftResult && rightOrig == rightResult)) {
            return new BinaryOpNode(node.getOperator(), leftResult, rightResult);
        }
        return node;
    }

    @Override
    public ConstantNode visitConstant(final ConstantNode node) {
        return node;
    }

    @Override
    public FormulaNode visitFormula(final FormulaNode node) {
        if (node instanceof UnaryOpNode) {
            return this.visitUnaryOperator((UnaryOpNode) node);
        }
        if (node instanceof BinaryOpNode) {
            return visitBinaryOperator((BinaryOpNode) node);
        }
        if (node instanceof QuantifierNode) {
            return visitQuantifier((QuantifierNode) node);
        }
        if (node instanceof PredicateNode) {
            return visitPredicate((PredicateNode) node);
        }
        return node;
    }

    @Override
    public FunctionNode visitFunction(final FunctionNode node) {
        List<TermNode> origArgs = node.getArguments();
        List<TermNode> argResults = visitArgList(origArgs);
        if (argResults != origArgs) {
            return new FunctionNode(node.getFunction(), argResults);
        }
        return node;
    }

    @Override
    public PredicateNode visitPredicate(final PredicateNode node) {
        List<TermNode> origArgs = node.getArguments();
        List<TermNode> argResults = visitArgList(origArgs);
        if (argResults != origArgs) {
            return new PredicateNode(node.getPredicate(), argResults);
        }
        return node;
    }

    @Override
    public QuantifierNode visitQuantifier(final QuantifierNode node) {
        FormulaNode subformulaResult = visitFormula(node.getSubformula());
        if (subformulaResult != node.getSubformula()) {
            return new QuantifierNode(node.getQuantifier(), node.getQuantifiedVar(), subformulaResult);
        }
        return node;
    }

    @Override
    public TermNode visitTerm(final TermNode node) {
        if (node instanceof ConstantNode) {
            return visitConstant((ConstantNode) node);
        }
        if (node instanceof VarNode) {
            return visitVar((VarNode) node);
        }
        if (node instanceof FunctionNode) {
            return visitFunction((FunctionNode) node);
        }
        return node;
    }

    @Override
    public UnaryOpNode visitUnaryOperator(final UnaryOpNode node) {
        FormulaNode subformulaResult = visitFormula(node.getSubformula());
        if (subformulaResult != node.getSubformula()) {
            return new UnaryOpNode(subformulaResult);
        }
        return node;
    }

    @Override
    public VarNode visitVar(final VarNode node) {
        return node;
    }

}

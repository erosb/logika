package logika.model.ast.visitor.impl.rewriter;

import java.util.Objects;
import java.util.Set;

import logika.model.Variable;
import logika.model.ast.FormulaNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.VarNode;
import logika.model.ast.visitor.impl.FreeVarCollector;

public class CleanVarConverter extends TreeRewriterBase {

    public static FormulaNode clean(final FormulaNode input) {
        return new CleanVarConverter(input).visitFormula(input);
    }

    private final Set<String> reservedNames;

    private final FormulaNode input;

    public CleanVarConverter(final FormulaNode input) {
        this.input = Objects.requireNonNull(input, "input cannot be null");
        this.reservedNames = FreeVarCollector.collect(input);
    }

    private String suggestReplacementFor(final String original) {
        char lastCh = original.charAt(original.length() - 1);
        int startFrom;
        String baseStr;
        if (Character.isDigit(lastCh)) {
            startFrom = lastCh + 48;
            baseStr = original.substring(0, original.length() - 1);
        } else {
            startFrom = 0;
            baseStr = original;
        }
        int i = startFrom;
        String rval;
        do {
            rval = baseStr + i;
            ++i;
        } while (reservedNames.contains(rval));
        return rval;
    }

    @Override
    public QuantifierNode visitQuantifier(final QuantifierNode node) {
        String varName = node.getQuantifiedVarName();
        if (reservedNames.contains(varName)) {
            String newName = suggestReplacementFor(varName);
            FormulaNode resultSub = VariableRenaming.rename(node.getSubformula(), varName, newName);
            reservedNames.add(newName);
            resultSub = visitFormula(resultSub);
            return new QuantifierNode(node.getToken(),
                    new VarNode(new Variable(newName, node.getQuantifiedVar().getType())), resultSub);
        }
        reservedNames.add(varName);
        return super.visitQuantifier(node);
    }

}

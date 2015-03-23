package logika.parser;

import logika.model.Language;
import logika.model.XMLLoader;
import logika.model.ast.ConstantNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.TermNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.VarNode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ASTBuildingTest {

    private Language lang;

    private void assertVarNode(final String expectedVarName, final TermNode node) {
        VarNode actual = (VarNode) node;
        Assert.assertEquals(expectedVarName, actual.getVar().getName());
    }

    @Before
    public void before() {
        lang = new XMLLoader(getClass().getResourceAsStream("/lang1.xml")).load();
    }

    @Test
    public void negatedPredicate() {
        FormulaNode output = parse("not(P1(C1, x))");
        Assert.assertTrue(output instanceof UnaryOpNode);
    }

    private FormulaNode parse(final String input) {
        return Parser.forString(input, lang).recognize();
    }

    @Test
    public void predicateWithConstant() {
        FormulaNode output = parse("P1(C1, x)");
        PredicateNode actual = (PredicateNode) output;
        Assert.assertEquals(lang.constantByName("C1"), ((ConstantNode) actual.getArgument(0)).getConstant());
    }

    @Test
    public void quantifiedFormula() {
        FormulaNode output = parse("all(x, P1(C1, x))");
        Assert.assertTrue(output instanceof QuantifierNode);
        QuantifierNode actual = (QuantifierNode) output;
        Assert.assertTrue(actual.getSubformula() instanceof PredicateNode);
    }

    @Test
    public void singlePredicate() {
        FormulaNode output = parse("P1(x, y)");
        PredicateNode actual = (PredicateNode) output;
        Assert.assertEquals(lang.predicateByName("P1"), actual.getPredicate());
        Assert.assertEquals(2, actual.getArguments().size());
        assertVarNode("x", actual.getArgument(0));
        assertVarNode("y", actual.getArgument(1));
    }

}

package logika.parser;

import logika.model.TestSupport;
import logika.model.ast.BinaryOpNode;
import logika.model.ast.ConstantNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.TermNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.VarNode;

import org.junit.Assert;
import org.junit.Test;

public class ASTBuildingTest {

    private final TestSupport testSupport = TestSupport.forLang1();

    private void assertVarNode(final String expectedVarName, final TermNode node) {
        VarNode actual = (VarNode) node;
        Assert.assertEquals(expectedVarName, actual.getVar().getName());
    }

    @Test
    public void binaryOpFormula() {
        FormulaNode output = testSupport.parseFormula("and(impl(P1(y, y), P2(C1, x)), all(x, not(P1(x, x))))");
        Assert.assertTrue(output instanceof BinaryOpNode);
    }

    @Test
    public void fictiveQuantifiedFormula() {
        FormulaNode output = testSupport.parseFormula("all(x, P1(C1, y))");
        Assert.assertTrue(output instanceof PredicateNode);
    }

    @Test
    public void negatedPredicate() {
        FormulaNode output = testSupport.parseFormula("not(P1(C1, x))");
        Assert.assertTrue(output instanceof UnaryOpNode);
    }

    @Test
    public void predicateWithConstant() {
        FormulaNode output = testSupport.parseFormula("P1(C1, x)");
        PredicateNode actual = (PredicateNode) output;
        Assert.assertEquals(testSupport.lang().constantByName("C1"),
                ((ConstantNode) actual.getArgument(0)).getConstant());
    }

    @Test
    public void quantifiedFormula() {
        FormulaNode output = testSupport.parseFormula("all(x, P1(C1, x))");
        Assert.assertTrue(output instanceof QuantifierNode);
        QuantifierNode actual = (QuantifierNode) output;
        Assert.assertTrue(actual.getSubformula() instanceof PredicateNode);
        Assert.assertEquals("x", actual.getQuantifiedVar().getVar().getName());
    }

    @Test
    public void singlePredicate() {
        FormulaNode output = testSupport.parseFormula("P1(x, y)");
        PredicateNode actual = (PredicateNode) output;
        Assert.assertEquals(testSupport.lang().predicateByName("P1"), actual.getPredicate());
        Assert.assertEquals(2, actual.getArguments().size());
        assertVarNode("x", actual.getArgument(0));
        assertVarNode("y", actual.getArgument(1));
    }

}

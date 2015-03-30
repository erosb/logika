package logika.model.ast.visitor.impl.rewriter;

import java.util.Arrays;
import java.util.List;

import logika.model.TestSupport;
import logika.model.ast.FormulaNode;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ImplicationEliminatorTest {

    @Parameters
    public static final List<Object[]> params() {
        return Arrays.<Object[]> asList(
                new Object[] { "impl(P1(x, x), P1(x, y))", "or(not(P1(x, x)), P1(x, y))" },
                new Object[] { "not(impl(P1(x, x), P1(x, y)))", "and(P1(x, x), not(P1(x, y)))" }
                );
    }

    private final String input;

    private final String expected;

    private final TestSupport testSupport = TestSupport.forLang1();

    public ImplicationEliminatorTest(final String input, final String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        FormulaNode in = testSupport.parseFormula(input);
        FormulaNode actual = ImplicationEliminator.eliminate(in);
        Assert.assertEquals(expected, testSupport.asString(actual));
    }

}

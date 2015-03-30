package logika.model.ast.visitor.impl.rewriter;

import java.util.Arrays;
import java.util.List;

import logika.model.TestSupport;
import logika.model.ast.FormulaNode;
import logika.model.ast.visitor.impl.rewriter.DuplicateNegationEliminator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DuplicateNegationEliminatorTest {

    @Parameters
    public static List<Object[]> params() {
        return Arrays.<Object[]> asList(
                new Object[] { "P1(x, y)", "P1(x, y)" },
                new Object[] { "not(not(P1(x, y)))", "P1(x, y)" },
                new Object[] { "not(not(not(not(P1(x, y)))))", "P1(x, y)" },
                new Object[] { "not(not(all(x, not(not(P1(x, y))))))", "all(x, P1(x, y))" }
                );
    }

    private final String input;

    private final String expected;

    private final TestSupport testSupport = TestSupport.forLang1();

    public DuplicateNegationEliminatorTest(final String input, final String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void test() {
        FormulaNode in = testSupport.parseFormula(input);
        FormulaNode actual = DuplicateNegationEliminator.eliminate(in);
        Assert.assertEquals(expected, testSupport.asString(actual));
    }

}

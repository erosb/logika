package logika.model.ast.visitor.impl;

import java.util.Arrays;
import java.util.List;

import logika.model.TestSupport;
import logika.model.ast.Node;
import logika.model.ast.visitor.impl.FreeVarDetector;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FreeVarDetectorTest {

    @Parameters
    public static List<Object[]> params() {
        return Arrays.<Object[]> asList(
                new Object[] { "P1(x, y)", "x", true },
                new Object[] { "P1(x, y)", "y", true },
                new Object[] { "and(P1(x, y), not(P1(u, v)))", "u", true },
                new Object[] { "and(P1(x, y), all(u, P1(u, v)))", "u", false },
                new Object[] { "all(u, P1(u, v))", "u", false }
                );
    }

    private final TestSupport testSupport = TestSupport.forLang1();

    private final String formula;

    private final String varName;

    private final boolean expected;

    public FreeVarDetectorTest(final String formula, final String varName, final boolean expected) {
        this.formula = formula;
        this.varName = varName;
        this.expected = expected;
    }

    @Test
    public void test() {
        Node input = testSupport.parse(formula);
        boolean actual = FreeVarDetector.hasFreeVar(input, varName);
        Assert.assertEquals(expected, actual);
    }
}

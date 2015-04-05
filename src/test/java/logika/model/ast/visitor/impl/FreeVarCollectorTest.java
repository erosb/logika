package logika.model.ast.visitor.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import logika.model.TestSupport;
import logika.model.ast.FormulaNode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FreeVarCollectorTest {

    @Parameters
    public static List<Object[]> params() {
        List<String> empty = Collections.emptyList();
        return Arrays.<Object[]> asList(
                new Object[] { "P", empty },
                new Object[] { "and(P3(x), P3(y))", Arrays.asList("x", "y") },
                new Object[] { "all(y, P3(y))", empty },
                new Object[] { "and(P3(x), all(y, P3(y)))", Arrays.asList("x") }
                );
    }

    private final String input;

    private final Collection<String> expectedFreeVars;

    private final TestSupport testSupport = TestSupport.forLang1();

    public FreeVarCollectorTest(final String input, final Collection<String> expectedFreeVars) {
        this.input = input;
        this.expectedFreeVars = expectedFreeVars;
    }

    @Test
    public void test() {
        FormulaNode in = testSupport.parseFormula(input);
        Collection<String> actual = FreeVarCollector.collect(in);
        TestSupport.assertCollectionEquals(expectedFreeVars, actual);
    }

}

package logika.model.ast.visitor;

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
public class VariableRenamingTest {

    @Parameters
    public static final List<Object[]> params() {
        return Arrays.<Object[]> asList(
                new Object[] { "P1(x, y)", "y", "z", "P1(x, z)" },
                new Object[] { "P1(x, f(x, y))", "y", "z", "P1(x, f(x, z))" },
                new Object[] { "any(y, P1(x, y))", "y", "z", "any(y, P1(x, y))" },
                new Object[] { "any(z, P1(y, z))", "y", "z", "any(z, P1(y, z))" }
                );
    }

    private final String input;

    private final String renameFrom;

    private final String renameTo;

    private final String expected;

    private final TestSupport testSupport = TestSupport.forLang1();

    public VariableRenamingTest(final String input, final String renameFrom, final String renameTo,
            final String expected) {
        this.input = input;
        this.renameFrom = renameFrom;
        this.renameTo = renameTo;
        this.expected = expected;
    }

    @Test
    public void test() {
        FormulaNode input = testSupport.parseFormula(this.input);
        FormulaNode actual = VariableRenaming.rename(input, renameFrom, renameTo);
        Assert.assertEquals(expected, testSupport.asString(actual));
    }
}

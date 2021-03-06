package logika.model.ast.visitor.impl.rewriter;

import java.util.Arrays;
import java.util.List;

import logika.model.TestSupport;

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

    public VariableRenamingTest(final String input, final String renameFrom, final String renameTo,
            final String expected) {
        this.input = input;
        this.renameFrom = renameFrom;
        this.renameTo = renameTo;
        this.expected = expected;
    }

    @Test
    public void test() {
        TestSupport.forLang1()
                .testRewriter((in) -> VariableRenaming.rename(in, renameFrom, renameTo))
                .with(input, expected);
    }
}

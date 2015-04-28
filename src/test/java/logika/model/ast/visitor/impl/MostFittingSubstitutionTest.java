package logika.model.ast.visitor.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import logika.model.TestSupport;
import logika.model.ast.FormulaNode;
import logika.model.ast.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MostFittingSubstitutionTest {

    @Parameters
    public static final List<Object[]> params() {
        return Arrays.<Object[]> asList(
                new Object[] { Arrays.asList("P", "P"), "P" },
                new Object[] { Arrays.asList("P", "Q"), null },
                new Object[] { Arrays.asList("P", "P", "Q"), null },
                new Object[] { Arrays.asList("and(P, Q)", "and(P, Q)"), "and(P, Q)" },
                new Object[] { Arrays.asList("and(P, Q)", "or(P, Q)"), null },
                new Object[] { Arrays.asList("and(P3(x), Q)", "and(P3(x), Q)"), "and(P3(x), Q)" },
                new Object[] { Arrays.asList("and(P3(x), Q)", "and(P3(y), Q)"), "and(P3(x), Q)" },
                new Object[] { Arrays.asList("and(P3(x), Q)", "and(P3(f(y, z)), Q)"), "and(P3(x), Q)" },
                new Object[] { Arrays.asList("and(P3(f(x, y)), Q)", "and(P3(z), Q)"), "and(P3(z), Q)" }
                );
    }

    private final List<String> rawInputFormulas;

    private final String rawExpected;

    private final TestSupport testSupport = TestSupport.forLang1();

    public MostFittingSubstitutionTest(final List<String> rawInputFormulas, final String rawExpected) {
        this.rawInputFormulas = rawInputFormulas;
        this.rawExpected = rawExpected;
    }

    @Test
    public void test() {
        List<Node> inputs = rawInputFormulas.stream()
                .map(testSupport::parseFormula)
                .collect(Collectors.toList());
        Optional<Node> actual = MostFittingSubstitution.determine(testSupport.lang(), inputs);
        assertThat(actual.isPresent(), equalTo(rawExpected != null));
        if (rawExpected != null) {
            FormulaNode expected = testSupport.parseFormula(rawExpected);
            assertThat(actual.get(), equalTo(expected));
        }
    }
}

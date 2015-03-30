package logika.model.ast.visitor;

import java.util.Arrays;
import java.util.List;

import logika.model.TestSupport;
import logika.model.ast.Node;
import logika.model.ast.visitor.impl.DefaultSerializerVisitor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DefaultSerializerVisitorTest {

    @Parameters
    public static final List<Object[]> params() {
        return Arrays.asList(
                new Object[] { "P1(x, y)" },
                new Object[] { "P1(C1, y)" },
                new Object[] { "P1(f(x, z), y)" },
                new Object[] { "not(P1(f(x, z), y))" },
                new Object[] { "and(P1(f(x, z), y), P1(f(x, z), yy))" },
                new Object[] { "all(x, P1(f(x, z), y))" }
                );
    }

    private final String inputStr;

    private final TestSupport testSupport = TestSupport.forLang1();

    public DefaultSerializerVisitorTest(final String inputStr) {
        this.inputStr = inputStr;
    }

    @Test
    public void serializePredWithVars() {
        Node input = testSupport.parse(inputStr);
        Assert.assertEquals(inputStr, new DefaultSerializerVisitor().serialize(input));
    }

}

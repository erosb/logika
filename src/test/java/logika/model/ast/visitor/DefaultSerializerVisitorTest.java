package logika.model.ast.visitor;

import java.util.Arrays;
import java.util.List;

import logika.model.Language;
import logika.model.XMLLoader;
import logika.model.ast.Node;
import logika.parser.Parser;

import org.junit.Assert;
import org.junit.Before;
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

    private Language lang;

    private final String inputStr;

    public DefaultSerializerVisitorTest(final String inputStr) {
        this.inputStr = inputStr;
    }

    @Before
    public void before() {
        lang = new XMLLoader(getClass().getResourceAsStream("/lang1.xml")).load();
    }

    private Node parse(final String input) {
        return Parser.forString(input, lang).recognize();
    }

    @Test
    public void serializePredWithVars() {
        Node input = parse(inputStr);
        Assert.assertEquals(inputStr, new DefaultSerializerVisitor().serialize(input));
    }

}

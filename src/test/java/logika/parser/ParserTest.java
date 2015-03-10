package logika.parser;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void andOrImplWorks() {
        Assert.assertEquals("", subject("and(P1(x, y), P2(x, y))").recognizeFormula());
        Assert.assertEquals("", subject("or(P1(x, y), P2(x, y))").recognizeFormula());
        Assert.assertEquals("", subject("impl(P1(x, y), P2(x, y))").recognizeFormula());
    }

    @Test
    public void predicateNotFound() {
        recognitionException("Z(x)", "Z is not a predicate");
    }

    private void recognitionException(final String input, final String expectedMessage) {
        try {
            subject(input).recognizeFormula();
            Assert.fail("did not throw exception [" + expectedMessage + "]");
        } catch (RecognitionException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void singleConstantResult() {
        Assert.assertEquals("C1 is a constant", subject("C1").recognizeFormula());
    }

    @Test
    public void singlePredicate() {
        Assert.assertEquals("", subject("P1(x, y)").recognizeFormula());
    }

    @Test
    public void singleVariableResult() {
        Assert.assertEquals("x is a free variable", subject("x").recognizeFormula());
    }

    private Parser subject(final String input) {
        return Parser.forString(input, getClass().getResourceAsStream("/lang1.xml"));
    }

}

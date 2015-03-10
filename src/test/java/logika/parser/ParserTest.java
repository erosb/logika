package logika.parser;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {

    @Test
    public void andOrImplWorks() {
        Assert.assertEquals("", subject("and(P1(x, y), P2(x, y))").recognize());
        Assert.assertEquals("", subject("or(P1(x, y), P2(x, y))").recognize());
        Assert.assertEquals("", subject("impl(P1(x, y), P2(x, y))").recognize());
    }

    @Test
    public void notOperatorWorks() {
        Assert.assertEquals("", subject("not(P1(x, y))").recognize());
    }

    @Test
    public void predicateNotFound() {
        recognitionException("Z(x)", "Z is not a predicate");
    }

    @Test
    public void quantifiersWork() {
        Assert.assertEquals("", subject("all(x, P1(x, y))").recognize());
    }

    private void recognitionException(final String input, final String expectedMessage) {
        try {
            subject(input).recognize();
            Assert.fail("did not throw exception [" + expectedMessage + "]");
        } catch (RecognitionException e) {
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    @Ignore
    public void singleConstantResult() {
        Assert.assertEquals("C1 is a constant", subject("C1").recognize());
    }

    @Test
    public void singlePredicate() {
        Assert.assertEquals("", subject("P1(x, y)").recognize());
    }

    @Test
    @Ignore
    public void singleVariableResult() {
        Assert.assertEquals("x is a free variable", subject("x").recognize());
    }

    private Parser subject(final String input) {
        return Parser.forString(input, getClass().getResourceAsStream("/lang1.xml"));
    }

}

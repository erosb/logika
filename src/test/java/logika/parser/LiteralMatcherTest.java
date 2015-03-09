package logika.parser;

import org.junit.Assert;
import org.junit.Test;

public class LiteralMatcherTest {

    @Test
    public void testMatchFailure() {
        LiteralMatcher subject = new LiteralMatcher("and");
        Assert.assertNull(subject.apply('a'));
        Assert.assertNull(subject.apply('n'));
        Assert.assertEquals(LiteralMatcher.NO_MATCH, subject.apply('y'));
    }

    @Test
    public void testMatchSuccess() {
        LiteralMatcher subject = new LiteralMatcher("and");
        Assert.assertNull(subject.apply('a'));
        Assert.assertNull(subject.apply('n'));
        Assert.assertEquals("and", subject.apply('d'));
    }

    @Test
    public void testSingleCharMatch() {
        LiteralMatcher subject = new LiteralMatcher("f");
        String actual = subject.apply('f');
        Assert.assertEquals("f", actual);
    }

}

package logika.parser;

import org.junit.Assert;
import org.junit.Test;

public class IdMatcherTest {

    @Test
    public void testRecognition() {
        IdMatcher subject = new IdMatcher();
        Assert.assertNull(subject.apply('a'));
        Assert.assertNull(subject.apply('n'));
        Assert.assertEquals("an", subject.apply(' '));
    }

}

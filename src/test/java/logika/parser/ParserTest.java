package logika.parser;

import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void singleConstantResult() {
        Assert.assertEquals("it is a constant", subject("C1").recognize());
    }

    private Parser subject(final String input) {
        return Parser.forString(input, getClass().getResourceAsStream("/lang1.xml"));
    }

}

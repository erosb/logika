package logika.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class IdMatcher implements Function<Character, String> {

    private static final Collection<String> KEYWORDS = Arrays.asList("and", "or", "impl", "not", "all", "any");

    private final StringBuilder recognized = new StringBuilder();

    @Override
    public String apply(final Character ch) {
        if (permittedChar(ch)) {
            recognized.append(ch.charValue());
            return null;
        } else {
            String rval = recognized.toString();
            if (KEYWORDS.contains(rval)) {
                return TokenMatcher.NO_MATCH;
            }
            return rval;
        }
    }

    private boolean permittedChar(final char ch) {
        return ('a' <= ch && ch <= 'z')
                || ('A' <= ch && ch <= 'Z')
                || (recognized.length() > 0 && '0' <= ch && ch <= '9');
    }

}

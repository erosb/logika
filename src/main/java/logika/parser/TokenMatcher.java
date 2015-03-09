package logika.parser;

import java.util.function.Function;

public interface TokenMatcher extends Function<Character, String> {

    public static final String NO_MATCH = "";

}

package logika.parser;

public class LiteralMatcher implements TokenMatcher {

    private final String literal;

    private int nextIdx = 0;

    public LiteralMatcher(final String literal) {
        this.literal = literal;
    }

    @Override
    public String apply(final Character ch) {
        if (literal.charAt(nextIdx) != ch) {
            return NO_MATCH;
        }
        ++nextIdx;
        if (nextIdx < literal.length()) {
            return null;
        }
        return literal;
    }

}

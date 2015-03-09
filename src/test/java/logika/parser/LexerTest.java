package logika.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LexerTest {

    private void assertTokenList(final List<Token> expected, final List<Token> actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("expected token count: " + expected.size() + ", actual: " + actual.size());
        }
        for (int i = 0; i < expected.size(); ++i) {
            Token expToken = expected.get(i);
            Token actToken = actual.get(i);
            if (!expToken.equals(actToken)) {
                throw new AssertionError("token #" + i + ": expected: " + expToken + "\tactual: " + actToken);
            }
        }
    }

    private List<Token> readAll(final Lexer input) {
        List<Token> rval = new ArrayList<>();
        Token token;
        while ((token = input.nextToken()) != null) {
            rval.add(token);
        }
        return rval;
    }

    private Lexer subject(final String input) {
        return new Lexer(new StringReader(input));
    }

    @Test
    public void test() {
        Lexer subject = subject(" and or ( )impl,");
        List<Token> actual = readAll(subject);
        List<Token> expected = Arrays.asList(new Token(TokenType.AND, "and"),
                new Token(TokenType.OR, "or"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.IMPL, "impl"),
                new Token(TokenType.COMMA, ","));
        assertTokenList(expected, actual);
    }

    @Test
    public void testIdentifier() {
        Lexer subject = subject("hello, world (and andy )");
        List<Token> actual = readAll(subject);
        List<Token> expected = Arrays.asList(new Token(TokenType.ID, "hello"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.ID, "world"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.AND, "and"),
                new Token(TokenType.ID, "andy"),
                new Token(TokenType.RPAREN, ")"));
        assertTokenList(expected, actual);
    }

    @Test
    public void testRealFormula() {
        Lexer subject = subject("P1(orr, y, z) and P2(c1, c2) ");
        List<Token> actual = readAll(subject);
        List<Token> expected = Arrays.asList(
                new Token(TokenType.ID, "P1"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.ID, "orr"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.ID, "y"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.ID, "z"),
                new Token(TokenType.RPAREN, ")"),
                new Token(TokenType.AND, "and"),
                new Token(TokenType.ID, "P2"),
                new Token(TokenType.LPAREN, "("),
                new Token(TokenType.ID, "c1"),
                new Token(TokenType.COMMA, ","),
                new Token(TokenType.ID, "c2"),
                new Token(TokenType.RPAREN, ")")
                );
        assertTokenList(expected, actual);
    }
}

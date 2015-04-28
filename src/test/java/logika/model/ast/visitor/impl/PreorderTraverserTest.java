package logika.model.ast.visitor.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import logika.model.TestSupport;
import logika.model.ast.Node;
import logika.parser.Lexer;
import logika.parser.Token;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class PreorderTraverserTest {

    private final class VerifierConsumer implements Consumer<Node> {

        final List<Token> expected;

        int i = 0;

        public VerifierConsumer(final List<Token> expected) {
            this.expected = expected;
        }

        @Override
        public void accept(final Node t) {
            assertEquals(expected.get(i++), t.getToken());
        }
    }

    private List<Token> getTokens(final String input) {
        Lexer lexer = new Lexer(new StringReader(input));
        Token t;
        List<Token> rval = new ArrayList<>();
        while ((t = lexer.nextToken()) != null) {
            rval.add(t);
        }
        return rval;
    }

    @Test
    public void test() {
        TestSupport testSupport = TestSupport.forLang1();
        String formula = "and(P, P1(C1, f(x, y)))";
        String tokens = "and P P1 C1 f x y";
        VerifierConsumer verifier = new VerifierConsumer(getTokens(tokens));
        PreorderTraverser.traverse(testSupport.parse(formula), verifier);
        assertThat(verifier.expected.size(), equalTo(verifier.i));
    }

}

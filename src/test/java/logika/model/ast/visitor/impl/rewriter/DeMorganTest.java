package logika.model.ast.visitor.impl.rewriter;

import logika.model.TestSupport;

import org.junit.Test;

public class DeMorganTest {

    @Test
    public void negationsDown() {
        TestSupport.forLang1()
                .testRewriter(DeMorgan::negationsDown)
                .with("not(and(P1(x, x), P1(x, x)))", "or(not(P1(x, x)), not(P1(x, x)))")
                .with("not(or(P1(x, x), P1(x, x)))", "and(not(P1(x, x)), not(P1(x, x)))");
    }

}

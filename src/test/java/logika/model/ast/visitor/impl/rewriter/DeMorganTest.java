package logika.model.ast.visitor.impl.rewriter;

import logika.model.TestSupport;

import org.junit.Test;

public class DeMorganTest {

    @Test
    public void negationsDown() {
        TestSupport.forLang1()
                .testRewriter(DeMorgan::negationsDown)
                .with("not(and(P, R))", "or(not(P), not(R))")
                .with("not(and(and(P, Q), R))", "or(or(not(P), not(Q)), not(R))")
                .with("not(or(P, P))", "and(not(P), not(P))")
                .with("not(and(and(P, Q), and(P, Q)))", "or(or(not(P), not(Q)), or(not(P), not(Q)))")
                .with("not(and(not(P), not(R)))", "or(P, R)")
                .with("not(all(x, P1(x, x)))", "any(x, not(P1(x, x)))")
                .with("not(and(any(y, P1(x, y)), any(y, and(P3(y), not(P1(x, a))))))",
                        "or(all(y, not(P1(x, y))), all(y, or(not(P3(y)), P1(x, a))))"); // not(and(P3(y), not(P1(x,
                                                                                        // a))))
    }
}

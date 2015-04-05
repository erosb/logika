package logika.model.ast.visitor.impl.rewriter;

import logika.model.TestSupport;

import org.junit.Test;

public class QuantifierRemoverTest {

    @Test
    public void existentialQuantifierExtraction() {
        TestSupport.forLang1()
                .testRewriter(QuantifierRemover::moveQuantifiersUp)
                .with("or(any(x, P3(x)), any(x, P3(x)))", "any(x, or(P3(x), P3(x)))");
    }

    @Test
    public void universalQuantifierExtraction() {
        TestSupport.forLang1()
        .testRewriter(QuantifierRemover::moveQuantifiersUp)
        .with("and(all(x, P3(x)), all(x, P3(x)))", "all(x, and(P3(x), P3(x)))")
        .with("and(all(x, P3(x)), all(y, P3(y)))", "all(x, and(P3(x), P3(x)))")
        .with("and(all(x, all(y, P1(x, y))), all(x, all(y, P2(x, y))))",
                        "all(x, all(y, and(P1(x, y), P2(x, y))))")
                .with("and(all(x, P3(x)), P1(y, y))", "all(x, and(P3(x), P1(y, y)))");
    }

}

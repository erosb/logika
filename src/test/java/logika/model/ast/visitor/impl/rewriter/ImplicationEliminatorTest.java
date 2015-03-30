package logika.model.ast.visitor.impl.rewriter;

import logika.model.TestSupport;

import org.junit.Test;

public class ImplicationEliminatorTest {

    @Test
    public void test() {
        TestSupport.forLang1()
                .testRewriter(ImplicationEliminator::eliminate)
                .with("impl(P1(x, x), P1(x, y))", "or(not(P1(x, x)), P1(x, y))")
        .with("not(impl(P1(x, x), P1(x, y)))", "and(P1(x, x), not(P1(x, y)))");
    }

}

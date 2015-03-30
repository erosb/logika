package logika.model.ast.visitor.impl.rewriter;

import logika.model.TestSupport;

import org.junit.Test;

public class DuplicateNegationEliminatorTest {

    @Test
    public void test() {
        TestSupport.forLang1()
        .testRewriter(DuplicateNegationEliminator::eliminate)
        .with("P1(x, y)", "P1(x, y)")
        .with("not(not(P1(x, y)))", "P1(x, y)")
        .with("not(not(not(not(P1(x, y)))))", "P1(x, y)")
        .with("not(not(all(x, not(not(P1(x, y))))))", "all(x, P1(x, y))");
    }

}

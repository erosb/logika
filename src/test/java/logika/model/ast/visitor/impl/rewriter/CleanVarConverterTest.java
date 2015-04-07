package logika.model.ast.visitor.impl.rewriter;

import logika.model.TestSupport;

import org.junit.Test;

public class CleanVarConverterTest {

    @Test
    public void test() {
        TestSupport
                .forLang1()
        .testRewriter(CleanVarConverter::clean)
        .with("and(all(x, P3(x)), any(x, P3(x)))", "and(all(x, P3(x)), any(x0, P3(x0)))")
        .with("and(all(x, and(any(x, P3(x)), P3(x))), P3(x))",
                        "and(all(x0, and(any(x1, P3(x1)), P3(x0))), P3(x))")
        .with("and(all(x, any(y, P1(x, y))), any(y, P1(x, y)))",
                        "and(all(x0, any(y, P1(x0, y))), any(y0, P1(x, y0)))")
                .with("and(P3(x0), or(any(x, P3(x)), all(x, P3(x))))",
                        "and(P3(x0), or(any(x, P3(x)), all(x1, P3(x1))))");
    }

}

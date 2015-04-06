package logika.model.ast.visitor.impl.rewriter;

import logika.model.TestSupport;

import org.junit.Test;

public class PrenexFormConverterTest {

    @Test
    public void test() {
        TestSupport
                .forLang1()
                .testRewriter(PrenexFormConverter::convert)
                .with("impl(all(x, and(any(y, P1(x, y)), any(y, not(impl(P3(y), P1(x, a)))))), not(all(x, any(y, impl(P2(y, x), P4(x, y))))))",
                        "any(x, all(y1, all(y, all(y0, or(or(not(P1(x, y)), or(not(P3(y0)), P1(x, a))), and(P2(y1, x), not(P4(x, y1))))))))");
    }

}

package logika.model;

import java.util.Objects;
import java.util.function.Function;

import logika.model.ast.FormulaNode;
import logika.model.ast.Node;
import logika.model.ast.visitor.impl.DefaultSerializerVisitor;
import logika.parser.Parser;

public class TestSupport {

    public class RewriterTestSupport {

        private final Function<FormulaNode, FormulaNode> subject;

        public RewriterTestSupport(final Function<FormulaNode, FormulaNode> subject) {
            this.subject = subject;
        }

        private void testWith(final String input, final String expectedOutput) {
            FormulaNode in = parseFormula(input);
            FormulaNode actual = subject.apply(in);
            if (!expectedOutput.equals(asString(actual))) {
                throw new AssertionError("expected: " + expectedOutput + "\tactual: " + asString(actual));
            }
        }

        public RewriterTestSupport with(final String input, final String expectedOutput) {
            testWith(input, expectedOutput);
            return this;
        }

        public void with(final String[]... args) {
            for (String[] arg : args) {
                testWith(arg[0], arg[1]);
            }
        }

    }

    public static final TestSupport forLang1() {
        return new TestSupport(new XMLLoader(TestSupport.class.getResourceAsStream("/lang1.xml")).load());
    }

    private final Language lang;

    public TestSupport(final Language lang) {
        this.lang = Objects.requireNonNull(lang, "lang cannot be null");
    }

    public String asString(final Node node) {
        return new DefaultSerializerVisitor().serialize(node);
    }

    public Language lang() {
        return lang;
    }

    public Node parse(final String input) {
        return Parser.forString(input, lang).recognize();
    }

    public FormulaNode parseFormula(final String input) {
        return Parser.forString(input, lang).recognize();
    }

    public RewriterTestSupport testRewriter(final Function<FormulaNode, FormulaNode> rewriter) {
        return new RewriterTestSupport(rewriter);
    }

}

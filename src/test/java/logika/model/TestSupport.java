package logika.model;

import java.util.Objects;

import logika.model.ast.FormulaNode;
import logika.model.ast.Node;
import logika.model.ast.visitor.impl.DefaultSerializerVisitor;
import logika.parser.Parser;

public class TestSupport {

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

}

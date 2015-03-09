package logika.parser;

import java.io.InputStream;
import java.io.StringReader;

import logika.model.Language;
import logika.model.XMLLoader;

public class Parser {

    public static final Parser forString(final String str, final InputStream langFile) {
        return new Parser(new Lexer(new StringReader(str + " ")), new XMLLoader(langFile).load());
    }

    private final Language lang;

    private final Lexer lexer;

    public Parser(final Lexer lexer, final Language lang) {
        this.lexer = lexer;
        this.lang = lang;
    }

    private void checkSingleLiteral() {
        lexer.nextToken();
    }

    public String recognize() {
        checkSingleLiteral();
        return "";
    }

}

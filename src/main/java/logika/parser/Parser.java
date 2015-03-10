package logika.parser;

import java.io.InputStream;
import java.io.StringReader;

import logika.model.Language;
import logika.model.Predicate;
import logika.model.Term;
import logika.model.Type;
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

    private String checkSingleLiteral(final Token token) {
        if (lang.constantExists(token.getText())) {
            return token.getText() + " is a constant";
        } else if (!lang.termExists(token.getText())) {
            return token.getText() + " is a free variable";
        }
        return null;
    }

    private void consume(final Token expected) {
        Token next = lexer.nextToken();
        if (next == null || !next.equals(expected)) {
            throw new RecognitionException("expected " + expected + ", was: " + next);
        }
    }

    public String recognizeFormula() {
        Token token = lexer.nextToken();
        Token lookahead = lexer.nextToken();
        if (lookahead == null) {
            return checkSingleLiteral(token);
        }
        lexer.pushBack(lookahead);
        TokenType tokenType = token.getType();
        if (tokenType == TokenType.ID) {
            return recognizePredicate(token.getText());
        } else if (tokenType == TokenType.AND
                || tokenType == TokenType.OR
                || tokenType == TokenType.IMPL) {
            recognizeTwoFormulas();
        }
        return "";
    }

    // private void recognizeFormula() {
    // Token token = lexer.nextToken();
    // if (token.getType() == TokenType.ID) {
    // recognizePredicate(token.getText());
    // }
    // }

    private Type recognizeParam() {
        Token token = lexer.nextToken();
        if (token.getType() != TokenType.ID) {
            throw new RecognitionException("expected ID, was: " + token);
        }
        Token lookahead = lexer.nextToken();
        lexer.pushBack(lookahead);
        if (lookahead.getType() == TokenType.LPAREN) {
            return recognizeTerm(token.getText());
        } else {
            return null; // variable
        }
    }

    private String recognizePredicate(final String predName) {
        consume(new Token(TokenType.LPAREN, "("));
        try {
            Predicate pred = lang.predicateByName(predName);
            int i = 0;
            for (Type t : pred.getArgTypes()) {
                if (i != 0) {
                    consume(new Token(TokenType.COMMA, ","));
                }
                Type actualType = recognizeParam();
                if (actualType != null && !t.equals(actualType)) {
                    throw new RecognitionException("param #" + i + " of " + predName + ": expected type: "
                            + t.getName() + ", actual type: " + actualType.getName());
                }
                ++i;
            }
            consume(new Token(TokenType.RPAREN, ")"));
        } catch (IllegalArgumentException e) {
            throw new RecognitionException(predName + " is not a predicate");
        }
        return "";
    }

    private Type recognizeTerm(final String termName) {
        try {
            Term term = lang.termByName(termName);
            consume(new Token(TokenType.LPAREN, "("));
            int i = 0;
            for (Type type : term.getArgTypes()) {
                if (i != 0) {
                    consume(new Token(TokenType.COMMA, ","));
                }
                Type actualType = recognizeParam();
                if (actualType != null && !type.equals(actualType)) {
                    throw new RecognitionException("param #" + i + " of " + termName + ": expected type: "
                            + type.getName() + ", actual type: " + actualType.getName());
                }
                ++i;
            }
            consume(new Token(TokenType.RPAREN, ")"));
            return term.getType();
        } catch (IllegalArgumentException e) {
            throw new RecognitionException("term " + termName + " not found");
        }
    }

    private void recognizeTwoFormulas() {
        consume(new Token(TokenType.LPAREN, "("));
        recognizeFormula();
        consume(new Token(TokenType.COMMA, ","));
        recognizeFormula();
        consume(new Token(TokenType.RPAREN, ")"));

    }
}

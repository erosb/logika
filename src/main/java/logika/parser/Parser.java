package logika.parser;

import java.io.InputStream;
import java.io.StringReader;

import logika.model.Constant;
import logika.model.Language;
import logika.model.Predicate;
import logika.model.Term;
import logika.model.Type;
import logika.model.XMLLoader;

public class Parser {

    private static final Token LPAREN = new Token(TokenType.LPAREN, "(");

    private static final Token RPAREN = new Token(TokenType.RPAREN, ")");

    private static final Token COMMA = new Token(TokenType.COMMA, ",");

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

    private void isReserved(final String tokenText) {
        if (lang.termExists(tokenText)) {
            throw new RecognitionException("symbol " + tokenText + " is a term, cannot be used as variable");
        }
        if (lang.predicateExists(tokenText)) {
            throw new RecognitionException("symbol " + tokenText
                    + " is a predicate, cannot be used as variable");
        }
    }

    public String recognize() {
        // Token token = lexer.nextToken();
        // Token lookahead = lexer.nextToken();
        // if (lookahead == null) {
        // return checkSingleLiteral(token);
        // }
        // lexer.pushBack(lookahead);
        recognizeFormula();
        return "";
    }

    private void recognizeFormula() {
        Token token = lexer.nextToken();
        TokenType tokenType = token.getType();
        if (token.getType() == TokenType.ID) {
            recognizePredicate(token.getText());
        } else if (tokenType == TokenType.AND
                || tokenType == TokenType.OR
                || tokenType == TokenType.IMPL) {
            recognizeTwoFormulas();
        } else if (tokenType == TokenType.NOT) {
            consume(LPAREN);
            recognizeFormula();
            consume(RPAREN);
        } else if (tokenType == TokenType.ALL || tokenType == TokenType.ANY) {
            consume(LPAREN);
            Token qualifVar = lexer.nextToken();
            if (qualifVar.getType() != TokenType.ID) {
                throw new RecognitionException("expected ID, was: " + qualifVar);
            }
            isReserved(qualifVar.getText());
            consume(COMMA);
            recognizeFormula();
            consume(RPAREN);
        }
    }

    private Type recognizeParam() {
        Token token = lexer.nextToken();
        if (token == null || token.getType() != TokenType.ID) {
            throw new RecognitionException("expected ID, was: " + token);
        }
        Token lookahead = lexer.nextToken();
        lexer.pushBack(lookahead);
        String tokenText = token.getText();
        if (lookahead.getType() == TokenType.LPAREN) {
            return recognizeTerm(tokenText);
        } else {
            try {
                Constant c = lang.constantByName(tokenText);
                return c.getType();
            } catch (IllegalArgumentException e) {
                isReserved(tokenText);
                return null; // variable
            }
        }
    }

    private String recognizePredicate(final String predName) {
        consume(LPAREN);
        try {
            Predicate pred = lang.predicateByName(predName);
            int i = 0;
            for (Type t : pred.getArgTypes()) {
                if (i != 0) {
                    consume(COMMA);
                }
                Type actualType = recognizeParam();
                if (actualType != null && !t.equals(actualType)) {
                    throw new RecognitionException("param #" + i + " of " + predName + ": expected type: "
                            + t.getName() + ", actual type: " + actualType.getName());
                }
                ++i;
            }
            consume(RPAREN);
        } catch (IllegalArgumentException e) {
            throw new RecognitionException(predName + " is not a predicate");
        }
        return "";
    }

    private Type recognizeTerm(final String termName) {
        try {
            Term term = lang.termByName(termName);
            consume(LPAREN);
            int i = 0;
            for (Type type : term.getArgTypes()) {
                if (i != 0) {
                    consume(COMMA);
                }
                Type actualType = recognizeParam();
                if (actualType != null && !type.equals(actualType)) {
                    throw new RecognitionException("param #" + i + " of " + termName + ": expected type: "
                            + type.getName() + ", actual type: " + actualType.getName());
                }
                ++i;
            }
            consume(RPAREN);
            return term.getType();
        } catch (IllegalArgumentException e) {
            throw new RecognitionException("term " + termName + " not found");
        }
    }

    private void recognizeTwoFormulas() {
        consume(LPAREN);
        recognizeFormula();
        consume(COMMA);
        recognizeFormula();
        consume(RPAREN);

    }
}

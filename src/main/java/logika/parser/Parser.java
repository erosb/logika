package logika.parser;

import java.io.InputStream;
import java.io.StringReader;

import logika.model.Function;
import logika.model.Language;
import logika.model.Predicate;
import logika.model.ScopingSymbolTable;
import logika.model.Type;
import logika.model.Variable;
import logika.model.XMLLoader;

public class Parser {

    public static final Parser forString(final String str, final InputStream langFile) {
        return new Parser(new Lexer(new StringReader(str + " ")), new XMLLoader(langFile).load());
    }

    private static final Token LPAREN = new Token(TokenType.LPAREN, "(");

    private static final Token RPAREN = new Token(TokenType.RPAREN, ")");

    private static final Token COMMA = new Token(TokenType.COMMA, ",");

    private final Language lang;

    private ScopingSymbolTable currScope;

    private final Lexer lexer;

    public Parser(final Lexer lexer, final Language lang) {
        this.lexer = lexer;
        this.currScope = new ScopingSymbolTable(lang);
        this.lang = lang;
    }

    private void consume(final Token expected) {
        Token next = lexer.nextToken();
        if (next == null || !next.equals(expected)) {
            throw new RecognitionException("expected " + expected + ", was: " + next);
        }
    }

    public String recognize() {
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
            currScope = new ScopingSymbolTable(currScope);
            consume(LPAREN);
            Token qualifVar = requireId();
            try {
                currScope.registerVariable(new Variable(qualifVar.getText(), null));
            } catch (IllegalArgumentException e) {
                throw new RecognitionException(e.getMessage());
            }
            consume(COMMA);
            recognizeFormula();
            consume(RPAREN);
            currScope = (ScopingSymbolTable) currScope.getParentScope();
        }
    }

    private void recognizeParam(final String ownerName, final int paramIdx, final Type expectedType) {
        Token token = requireId();
        Token lookahead = lexer.nextToken();
        lexer.pushBack(lookahead);
        String tokenText = token.getText();
        Type actualType;
        if (lookahead.getType() == TokenType.LPAREN) {
            actualType = recognizeFunction(tokenText);
        } else {
            if (currScope.constantExists(tokenText)) {
                actualType = lang.constantByName(tokenText).getType();
            } else if (currScope.varExists(tokenText)) {
                actualType = currScope.varByName(tokenText).getType();
                if (actualType == null) {
                    currScope.setVarType(tokenText, expectedType);
                    actualType = expectedType;
                }
            } else {
                currScope.registerVariable(new Variable(tokenText, expectedType));
                actualType = expectedType;
            }
        }
        if (!actualType.equals(expectedType)) {
            throw new RecognitionException("param #" + paramIdx + " of " + ownerName + ": expected type: "
                    + expectedType.getName() + ", actual type: " + actualType.getName());
        }
    }

    private String recognizePredicate(final String predName) {
        consume(LPAREN);
        Predicate pred;
        try {
            pred = lang.predicateByName(predName);
        } catch (IllegalArgumentException e) {
            throw new RecognitionException(predName + " is not a predicate");
        }
        int i = 0;
        for (Type t : pred.getArgTypes()) {
            if (i != 0) {
                consume(COMMA);
            }
            recognizeParam(predName, i, t);
            ++i;
        }
        consume(RPAREN);
        return "";
    }

    private Type recognizeFunction(final String functionName) {
        try {
            Function function = lang.functionByName(functionName);
            consume(LPAREN);
            int i = 0;
            for (Type type : function.getArgTypes()) {
                if (i != 0) {
                    consume(COMMA);
                }
                recognizeParam(functionName, i, type);
                ++i;
            }
            consume(RPAREN);
            return function.getType();
        } catch (IllegalArgumentException e) {
            throw new RecognitionException("function " + functionName + " not found");
        }
    }

    private void recognizeTwoFormulas() {
        consume(LPAREN);
        recognizeFormula();
        consume(COMMA);
        recognizeFormula();
        consume(RPAREN);

    }

    private Token requireId() {
        Token token = lexer.nextToken();
        if (token == null || !token.getType().equals(TokenType.ID)) {
            throw new RecognitionException("expected ID, was: " + token);
        }
        return token;
    }
}

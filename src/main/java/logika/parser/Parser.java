package logika.parser;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logika.model.Constant;
import logika.model.Function;
import logika.model.Language;
import logika.model.Predicate;
import logika.model.ScopingSymbolTable;
import logika.model.Type;
import logika.model.Variable;
import logika.model.XMLLoader;
import logika.model.ast.BinaryOpNode;
import logika.model.ast.ConstantNode;
import logika.model.ast.FormulaNode;
import logika.model.ast.FunctionNode;
import logika.model.ast.PredicateNode;
import logika.model.ast.QuantifierNode;
import logika.model.ast.TermNode;
import logika.model.ast.UnaryOpNode;
import logika.model.ast.VarNode;

public class Parser {

    public static final Parser forString(final String str, final InputStream langFile) {
        return forString(str, new XMLLoader(langFile).load());
    }

    public static final Parser forString(final String str, final Language lang) {
        return new Parser(new Lexer(new StringReader(str + " ")), lang);
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

    public FormulaNode recognize() {
        return recognizeFormula();
    }

    private FormulaNode recognizeFormula() {
        Token token = lexer.nextToken();
        TokenType tokenType = token.getType();
        if (token.getType() == TokenType.ID) {
            return recognizePredicate(token.getText());
        } else if (currScope.isBinaryOperator(tokenType)) {
            return recognizeTwoFormulas(token);
        } else if (tokenType == TokenType.NOT) {
            consume(LPAREN);
            FormulaNode arg = recognizeFormula();
            consume(RPAREN);
            return new UnaryOpNode(arg);
        } else if (tokenType == TokenType.ALL || tokenType == TokenType.ANY) {
            currScope = new ScopingSymbolTable(currScope);
            consume(LPAREN);
            Token quantifVar = requireId();
            try {
                currScope.declareVar(quantifVar.getText());
            } catch (IllegalArgumentException e) {
                throw new RecognitionException(e.getMessage());
            }
            consume(COMMA);
            FormulaNode arg = recognizeFormula();
            consume(RPAREN);
            try {
                Variable quantifiedVar = currScope.varByName(quantifVar.getText());
                VarNode varNode = new VarNode(quantifiedVar);
                return new QuantifierNode(token, varNode, arg);
            } catch (IllegalArgumentException e) {
                return arg;
            } finally {
                currScope = (ScopingSymbolTable) currScope.getParentScope();
            }
        } else {
            throw new RecognitionException("unknown token " + token);
        }
    }

    private FunctionNode recognizeFunction(final Token functionNameToken) {
        String functionName = functionNameToken.getText();
        try {
            Function function = lang.functionByName(functionName);
            List<TermNode> arguments = recognizeParamList(functionName, function.getArgTypes());
            return new FunctionNode(functionNameToken, function, arguments);
        } catch (IllegalArgumentException e) {
            throw new RecognitionException("function " + functionName + " not found");
        }
    }

    private TermNode recognizeParam(final String ownerName, final int paramIdx, final Type expectedType) {
        Token token = requireId();
        Token lookahead = lexer.nextToken();
        lexer.pushBack(lookahead);
        String tokenText = token.getText();
        Type actualType = null;
        TermNode rval;
        if (lookahead.getType() == TokenType.LPAREN) {
            rval = recognizeFunction(token);
        } else {
            if (currScope.constantExists(tokenText)) {
                Constant constant = lang.constantByName(tokenText);
                rval = new ConstantNode(token, constant);
            } else if (currScope.varDeclared(tokenText)) {
                Variable var = currScope.setVarType(tokenText, expectedType);
                actualType = expectedType;
                rval = new VarNode(var);
            } else if (currScope.varExists(tokenText)) {
                Variable var = currScope.varByName(tokenText);
                actualType = var.getType();
                if (actualType == null) {
                    var = currScope.setVarType(tokenText, expectedType);
                    actualType = expectedType;
                }
                rval = new VarNode(var);
            } else {
                Variable var = new Variable(tokenText, expectedType);
                rval = new VarNode(var);
                currScope.registerVariable(var);
                actualType = expectedType;
            }
        }
        if (actualType == null) {
            actualType = rval.getType();
        }
        if (!actualType.equals(expectedType)) {
            throw new RecognitionException("param #" + paramIdx + " of " + ownerName + ": expected type: "
                    + expectedType.getName() + ", actual type: " + actualType.getName());
        }
        return rval;
    }

    private List<TermNode> recognizeParamList(final String ownerName, final List<Type> argTypes) {
        consume(LPAREN);
        int i = 0;
        List<TermNode> arguments = new ArrayList<>(argTypes.size());
        for (Type t : argTypes) {
            if (i != 0) {
                consume(COMMA);
            }
            arguments.add(recognizeParam(ownerName, i, t));
            ++i;
        }
        consume(RPAREN);
        return arguments;
    }

    private PredicateNode recognizePredicate(final String predName) {
        Predicate pred;
        try {
            pred = lang.predicateByName(predName);
        } catch (IllegalArgumentException e) {
            throw new RecognitionException(predName + " is not a predicate");
        }
        List<Type> argTypes = pred.getArgTypes();
        List<TermNode> arguments;
        if (argTypes.isEmpty()) {
            arguments = Collections.emptyList();
        } else {
            arguments = recognizeParamList(predName, argTypes);
        }
        PredicateNode rval = new PredicateNode(pred, arguments);
        return rval;
    }

    private FormulaNode recognizeTwoFormulas(final Token token) {
        consume(LPAREN);
        FormulaNode left = recognizeFormula();
        consume(COMMA);
        FormulaNode right = recognizeFormula();
        consume(RPAREN);
        return new BinaryOpNode(token, left, right);

    }

    private Token requireId() {
        Token token = lexer.nextToken();
        if (token == null || !token.getType().equals(TokenType.ID)) {
            throw new RecognitionException("expected ID, was: " + token);
        }
        return token;
    }
}

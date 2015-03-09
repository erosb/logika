package logika.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;

public class Lexer {

    private final Reader reader;

    private boolean readerFinished = false;

    private int lineNumber = 1;

    private int charPosInLine = 1;

    private boolean trailingSpaceWasYielded;

    public Lexer(final Reader reader) {
        if (!reader.markSupported()) {
            throw new IllegalArgumentException("this stream does not support marks");
        }
        this.reader = reader;
    }

    public Token nextToken() {
        if (readerFinished) {
            return null;
        }
        try {
            skipWhitespaces();
            reader.mark(Integer.MAX_VALUE);
            for (TokenType tokenType : TokenType.values()) {
                Function<Character, String> matcher = tokenType.get();
                String success = null;
                int ch;
                while ((ch = readch()) != -1) {
                    success = matcher.apply((char) ch);
                    if (success != null) {
                        break;
                    }
                }
                if (ch == -1) {
                    if (tokenType == TokenType.ID) {
                        // System.out.println("reached -1 with ID");
                        success = matcher.apply(' ');
                        reader.skip(-1);
                    } else {
                        readerFinished = true;
                    }
                }
                if (TokenMatcher.NO_MATCH.equals(success)) {
                    reader.reset();
                } else {
                    if (tokenType == TokenType.ID) {
                        reader.skip(-1);
                    }
                    // System.out.println("returning token '" + success + "'");
                    if (success == null) {
                        return null;
                    }
                    return new Token(tokenType, success, lineNumber, charPosInLine);
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int readch() throws IOException {
        if (trailingSpaceWasYielded) {
            return -1;
        }
        int rval = reader.read();
        if (rval == -1) {
            trailingSpaceWasYielded = true;
            return ' ';
        }
        return rval;
    }

    private void skipWhitespaces() throws IOException {
        reader.mark(Integer.MAX_VALUE);
        int wch;
        while ((wch = readch()) == ' ' || wch == '\t' || wch == '\n') {
            if (wch == '\n') {
                ++lineNumber;
                charPosInLine = 1;
            } else {
                ++charPosInLine;
            }
            reader.mark(Integer.MAX_VALUE);
        }
        reader.reset();
    }

}

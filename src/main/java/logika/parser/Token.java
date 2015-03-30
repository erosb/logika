package logika.parser;

public class Token {

    public static Token and() {
        return new Token(TokenType.AND, "and");
    }

    public static Token not() {
        return new Token(TokenType.NOT, "not");
    }

    public static Token or() {
        return new Token(TokenType.OR, "or");
    }

    private final TokenType type;

    private final String text;

    private final int lineNumber;

    private final int charPosInLine;

    public Token(final TokenType type, final String text) {
        this.type = type;
        this.text = text;
        this.lineNumber = -1;
        this.charPosInLine = -1;
    }

    public Token(final TokenType type, final String text, final int lineNumber, final int charPosInLine) {
        this.type = type;
        this.text = text;
        this.lineNumber = lineNumber;
        this.charPosInLine = charPosInLine;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Token other = (Token) obj;
        if (text == null) {
            if (other.text != null) {
                return false;
            }
        } else if (!text.equals(other.text)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    public int getCharPosInLine() {
        return charPosInLine;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getText() {
        return text;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Token [" + type + ", " + text + "]";
    }

}

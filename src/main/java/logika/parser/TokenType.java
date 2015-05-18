package logika.parser;

import java.util.function.Function;
import java.util.function.Supplier;

public enum TokenType implements Supplier<Function<Character, String>> {

    ID {

        @Override
        public Function<Character, String> get() {
            return new IdMatcher();
        }

    },
    AND {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("and");
        }

    },
    OR {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("or");
        }

    },
    IMPL {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("impl");
        }

    },
    NOT {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("not");
        }

    },
    ANY {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("any");
        }

    },
    ALL {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("all");
        }

    },
    LPAREN {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("(");
        }

    },
    RPAREN {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher(")");
        }

    },
    COMMA {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher(",");
        }

    },
    ARROW {

        @Override
        public Function<Character, String> get() {
            return new LiteralMatcher("->");
        }
        
    };

    /*
     * , OR, IMPL, NOT, ANY, ALL, LPAREN, RPAREN, COMMA, ID;
     */

}

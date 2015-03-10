package logika;

import logika.parser.Parser;
import logika.parser.RecognitionException;

public class Main {

    public static void main(final String[] args) {
        String input = args[0];
        String output;
        try {
            output = Parser.forString(input, Main.class.getResourceAsStream("/lang1.xml")).recognize();
        } catch (RecognitionException e) {
            output = e.getMessage();
        }
        System.out.println(output);
    }

}

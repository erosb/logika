package logika;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import logika.parser.LexerException;
import logika.parser.Parser;
import logika.parser.RecognitionException;

public class Main {

    public static void main(final String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.out.println("Usage: java -jar logika.jar <input-text> [<path-to-langfile>]");
            return;
        }
        String input = args[0];
        InputStream langStream;
        if (args.length >= 2) {
            String langFilePath = args[1];
            File langFile = new File(langFilePath);
            langStream = new FileInputStream(langFile);
        } else {
            langStream = Main.class.getResourceAsStream("/lang1.xml");
        }
        String output;
        try {
            output = Parser.forString(input, langStream).recognize();
        } catch (RecognitionException | LexerException e) {
            output = e.getMessage();
        }
        System.out.println(output);
    }

}

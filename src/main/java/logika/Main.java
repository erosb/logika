package logika;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import logika.model.Language;
import logika.model.XMLLoader;
import logika.model.ast.FormulaNode;
import logika.model.ast.visitor.impl.rewriter.PrenexFormConverter;
import logika.parser.LexerException;
import logika.parser.Parser;
import logika.parser.RecognitionException;

public class Main {

    public static void main(final String[] args) throws FileNotFoundException {
        InputStream langStream;
        if (args.length >= 1) {
            String langFilePath = args[0];
            File langFile = new File(langFilePath);
            langStream = new FileInputStream(langFile);
        } else {
            langStream = Main.class.getResourceAsStream("/lang1.xml");
        }
        Language lang = new XMLLoader(langStream).load();
        String line, lastCommand = null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(PROMPT);
            while ((line = in.readLine()) != null) {
                if (line.charAt(0) == '#') {
                    continue;
                }
                String[] segments = line.split(" ");
                String arg;
                if (segments[0].equals(CMD_PARSE)) {
                    lastCommand = CMD_PARSE;
                    arg = Arrays.asList(segments).subList(1, segments.length).stream().collect(Collectors.joining());
                } else if (segments[0].equals(CMD_PRENEX)) {
                    lastCommand = CMD_PRENEX;
                    arg = Arrays.asList(segments).subList(1, segments.length).stream().collect(Collectors.joining());
                } else if (segments[0].equals("exit") || segments[0].equals("q")) {
                    arg = null;
                    lastCommand = "exit";
                } else {
                    arg = line;
                }
                if (lastCommand == null) {
                    System.out.println("missing command");
                }
                if (lastCommand.equals(CMD_PARSE)) {
                    parse(arg, lang);
                } else if (lastCommand.equalsIgnoreCase(CMD_PRENEX)) {
                    prenexize(arg, lang);
                } else if (lastCommand.equals("exit") || lastCommand.equals("q")) {
                    System.out.println();
                    System.exit(0);
                }
                System.out.print(PROMPT);
            }
            System.out.println();
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }

    private static void parse(final String input, final Language lang) {
        try {
            Parser.forString(input, lang).recognize();
        } catch (RecognitionException | LexerException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void prenexize(final String input, final Language lang) {
        try {
            FormulaNode in = Parser.forString(input, lang).recognize();
            FormulaNode output = PrenexFormConverter.convert(in);
            System.out.println(output);
        } catch (RecognitionException | LexerException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String PROMPT = "> ";

    private static final String CMD_PRENEX = "prenex";

    private static final String CMD_PARSE = "parse";

}

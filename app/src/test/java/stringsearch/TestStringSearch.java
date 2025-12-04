package stringsearch;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.ArrayList;

public class TestStringSearch {

    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent =
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private final String inputFilePath = 
        "src/test/java/stringsearch/input/input.txt";

    private static ArrayList<String> getMatchedLines(String s) {
        ArrayList<String> res = new ArrayList<>();

        Matcher m = Pattern.compile("(?m)^[0-9]+:.*$").matcher(s);

        while(m.find()) {
            res.add(m.group());
        }

        return res;
    }

    @Before
    public void setupOutputStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreOutputStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void incorrectArgs() {
        String args[] = {"straight", "up", "illegal", "!"};

        StringSearch.main(args);

        assertEquals("""
            Incorrect number of arguments.
            Usage: search PATTERN FILE
            """,
            outContent.toString());
    }


    @Test
    public void fileNotFound() {

        String invalidPath = "src/test/java/stringsearch/input/not_found.txt";

        String args[] = {"some string", invalidPath};
        StringSearch.main(args);

        assertEquals("Could not find file " + invalidPath + "\n",
            outContent.toString());
    }

    @Test
    public void matchOne() {
        String args[] = {"there is text", inputFilePath};

        StringSearch.main(args);

        ArrayList<String> matchedLines = getMatchedLines(outContent.toString());

        assertEquals(1, matchedLines.size());
        assertEquals("0: On this line, there is text.",
            matchedLines.get(0));
    }

    @Test
    public void matchMultiple() {
        String args[] = {"On this line", inputFilePath};

        StringSearch.main(args);

        ArrayList<String> matchedLines = getMatchedLines(outContent.toString());

        assertEquals(3, matchedLines.size());
        assertEquals(
            """
            0: On this line, there is text.
            1: On this line, there is the textual representation of a cat. Meow.
            2: On this line, there is a line.""",
            String.join("\n", matchedLines)
        );
    }

    @Test
    public void matchNone() {
        String searchString = "404: string not found";
        String args[] = {searchString, inputFilePath};

        StringSearch.main(args);

        ArrayList<String> matchedLines = getMatchedLines(outContent.toString());

        assertEquals(0, matchedLines.size());
        assertEquals("Found no matches for \"" + searchString + "\" in file "
            + inputFilePath + "\n", outContent.toString());
    }
}

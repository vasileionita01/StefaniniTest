package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void testMain_InsufficientArgs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Main.main(new String[]{"file.txt"});
        String expectedMessage = "Open from CommandLine: java -jar jarFileName.jar <file_path> -top= <top_phrases> -phraseSize= <phrase_size>";
        assertEquals(expectedMessage, outContent.toString().trim());
    }
    @Test
    void testParseArgs_ValidArgs() {
        String[] args = {"out/artifacts/StefaniniTest_jar/test.txt", "-top=5", "-phraseSize=3"};
        Main.main(args);
        assertEquals(5, Main.top);
        assertEquals(3, Main.phraseSize);
    }

    @Test
    void testParseArgs_InvalidTop() {
        String[] args = {"test.txt", "-top=-5", "-phraseSize=3"};
        Main.main(args);
        assertEquals(-5, Main.top);
        assertEquals(0, Main.phraseSize);
    }

    @Test
    void testParseArgs_InvalidPhraseSize() {
        String[] args = {"test.txt", "-top=5", "-phraseSize=-3"};
        Main.main(args);
        assertEquals(-3, Main.phraseSize);
    }

    @Test
    void testParseArgs_NonIntegerValues() {
        Main.top = 0;
        Main.phraseSize = 0;
        String[] args = {"test.txt", "-top=abc", "-phraseSize=xyz"};
        Main.main(args);
        assertEquals(0, Main.top);
        assertEquals(0, Main.phraseSize);
    }

    @Test
    void testReadText() {
        String filePath = "out/artifacts/StefaniniTest_jar/test.txt";
        String text = Main.readText(filePath);
        assertNotNull(text);
        assertFalse(text.isEmpty());
    }
    @Test
    void testReadText_FileNotFound() {
        String filePath = "fisierInexistent.txt";
        String expectedErrorMessage = "Error reading file: " + filePath + " (The system cannot find the file specified)";
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        String text = Main.readText(filePath);
        assertEquals(expectedErrorMessage, errContent.toString().trim());
        assertEquals("", text);
    }
    @Test
    void testNumarCuvinte() {
        String text = "Acesta este un test de numarat cuvinte.";
        int expected = 7;
        int actual = Main.numberOfWords(text);
        assertEquals(expected, actual);
    }

    @Test
    void testNumarFraze() {
        String text = "Aceasta este o fraza de test. Alta fraza de test!";
        int expected = 2;
        int actual = Main.numberOfPhrases(text);
        assertEquals(expected, actual);
    }

    @Test
    void testTopPhrases() {
        String text = "Aceasta este o fraza";
        int top = 2;
        int phraseSize = 3;
        Map<String, Integer> topPhrases = Main.topPhrases(text, top, phraseSize);
        assertNotNull(topPhrases);
        assertEquals(2, topPhrases.size());
        assertTrue(topPhrases.containsKey("Aceasta este o"));
        assertTrue(topPhrases.containsKey("este o fraza"));
        assertEquals(1, topPhrases.get("Aceasta este o"));
        assertEquals(1, topPhrases.get("este o fraza"));
    }

}

package bg.sofia.uni.fmi.mjt.spellchecker;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NaiveSpellCheckerTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String METADATA_HEADER = "= = = Metadata = = =" + LINE_SEPARATOR;
    private static final String FINDINGS_HEADER = "= = = Findings = = =" + LINE_SEPARATOR;

    private static final String dictionaryWordsExample = "    hey" + LINE_SEPARATOR
            + " -hello? " + LINE_SEPARATOR
            + "   abalone .;  " + LINE_SEPARATOR
            + "-!@#balloons,." + LINE_SEPARATOR
            + " .zerO" + LINE_SEPARATOR
            + "    b  " + LINE_SEPARATOR
            + "    ,AnGrY,  " + LINE_SEPARATOR
            + "phello" + LINE_SEPARATOR
            + "  chocolates " + LINE_SEPARATOR
            + " foot's " + LINE_SEPARATOR
            + "what::;" + LINE_SEPARATOR
            + "food" + LINE_SEPARATOR
            + "modernistic" + LINE_SEPARATOR
            + "chello" + LINE_SEPARATOR
            + "yellow" + LINE_SEPARATOR
            + "   But" + LINE_SEPARATOR
            + "mellow" + LINE_SEPARATOR;

    private static final Set<String> dictionaryWordsFromExample = Set.of("abalone", "angry", "balloons", "but", "chello",
            "chocolates", "food", "foot's", "hello", "hey", "mellow", "modernistic", "phello", "what", "yellow",
            "zero");

    private static final String stopWordsExample = "  a  " + LINE_SEPARATOR
            + " bY" + LINE_SEPARATOR
            + "aren't " + LINE_SEPARATOR
            + "we've  " + LINE_SEPARATOR
            + "IT" + LINE_SEPARATOR
            + " Lot" + LINE_SEPARATOR
            + "I" + LINE_SEPARATOR
            + " iS    " + LINE_SEPARATOR
            + "but " + LINE_SEPARATOR
            + " goT " + LINE_SEPARATOR
            + "but" + LINE_SEPARATOR
            + " theirs'  " + LINE_SEPARATOR;

    private static final Set<String> stopWordsFromExample = Set.of("a", "aren't", "but", "by", "got",
            "i", "is", "it", "lot", "theirs'", "we've");

    private static final String testingText =
            "WE've got theirs' melloww  BUT iss yeellow !," + LINE_SEPARATOR + LINE_SEPARATOR
                    + "WHAAT? Don't be anggrry, it is a food :)";

    private final Reader dictionaryReaderExample = new StringReader(dictionaryWordsExample);
    private final Reader stopWordsReaderExample = new StringReader(stopWordsExample);
    private final Reader testingTextReader = new StringReader(testingText);
    private final Writer writer = new StringWriter();
    private final BufferedWriter testingTextWriter = new BufferedWriter(writer);

    @Test(expected = IllegalArgumentException.class)
    public void testNaiveSpellCheckerWhenDictionaryReaderIsNull() {
        String stopWords = "";
        Reader readerStopWords = new StringReader(stopWords);
        new NaiveSpellChecker(null, readerStopWords);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNaiveSpellCheckerWhenStopWordsReaderIsNull() {
        String dictionaryWords = "";
        Reader readerDictionaryWords = new StringReader(dictionaryWords);
        new NaiveSpellChecker(readerDictionaryWords, null);
    }

    @Test
    public void testGetDictionaryAndStopWordsWhenEmpty() {
        String emptyWord = "";
        Reader readerDictionaryWords = new StringReader(emptyWord);
        Reader readerStopWords = new StringReader(emptyWord);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        assertTrue("Dictionary should be empty because only empty word is added",
                spellChecker.getDictionaryWords().isEmpty());
        assertTrue("Stop-words should be empty because only empty word is added",
                spellChecker.getStopWords().isEmpty());
    }

    @Test
    public void testGetDictionaryAndStopWordsOnlySymbols() {
        String symbolsWord = "-,@?!";
        Reader readerDictionaryWords = new StringReader(symbolsWord);
        Reader readerStopWords = new StringReader(symbolsWord);
        Set<String> words = Set.of("-,@?!");
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        assertTrue("Dictionary shouldn't contain only symbols",
                spellChecker.getDictionaryWords().isEmpty());
        assertEquals("Stop-words can contain only symbols",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsOnlyOneLetter() {
        String letter = "a";
        Reader readerDictionaryWords = new StringReader(letter);
        Reader readerStopWords = new StringReader(letter);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("a");
        assertTrue("Dictionary shouldn't contain words with less than 2 letters",
                spellChecker.getDictionaryWords().isEmpty());
        assertEquals("Stop-words can contain 1-lettered words",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsTwoLetters() {
        String word = "ab";
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("ab");
        assertEquals("Dictionary can contain 2-lettered words",
                words, spellChecker.getDictionaryWords());
        assertEquals("Stop-words can contain 2-lettered words",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithOneCorrectLowercaseWord() {
        String word = "simona" + LINE_SEPARATOR;
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("simona");
        assertEquals("Dictionary can contain lowercase words",
                words, spellChecker.getDictionaryWords());
        assertEquals("Stop-words can contain lowercase words",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithOneCorrectUppercaseWord() {
        String word = "SIMONA" + LINE_SEPARATOR;
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("simona");
        assertEquals("Dictionary should contain case insensitive words",
                words, spellChecker.getDictionaryWords());
        assertEquals("Stop-words should contain case insensitive words",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithCorrectSymbolsWord() {
        String word = "SIM-O'N-'A" + LINE_SEPARATOR;
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("sim-o'n-'a");
        assertEquals("Dictionary can contain non-alphanumeric symbols between letters",
                words, spellChecker.getDictionaryWords());
        assertEquals("Stop-words can contain non-alphanumeric symbols between letters",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithOneCorrectWordRepeatTwoTimes() {
        String word = "SIM-O'N-'A";
        String text = "";
        text += word + LINE_SEPARATOR;
        text += word + LINE_SEPARATOR;
        Reader readerDictionaryWords = new StringReader(text);
        Reader readerStopWords = new StringReader(text);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("sim-o'n-'a");
        assertEquals("Dictionary should contain unique words",
                words, spellChecker.getDictionaryWords());
        assertEquals("Stop-words should contain unique words",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithLeadingWhitespaces() {
        String word = "   siMONA" + LINE_SEPARATOR;
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("simona");
        assertEquals("Dictionary shouldn't contain leading whitespaces",
                words, spellChecker.getDictionaryWords());
        assertEquals("Stop-words shouldn't contain leading whitespaces",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithTrailingWhitespaces() {
        String word = "siMONA   ";
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> words = Set.of("simona");
        assertEquals("Dictionary shouldn't contain trailing whitespaces",
                words, spellChecker.getDictionaryWords());
        assertEquals("Stop-words shouldn't contain trailing whitespaces",
                words, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithLeadingSymbols() {
        String word = " ,?> -siMONA" + LINE_SEPARATOR;
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> dictionaryWords = Set.of("simona");
        Set<String> stopWords = Set.of(",?> -simona");
        assertEquals("Dictionary shouldn't contain leading symbols",
                dictionaryWords, spellChecker.getDictionaryWords());
        assertEquals("Stop-words can contain leading symbols",
                stopWords, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithTrailingSymbols() {
        String word = "siMONA;?.,!   " + LINE_SEPARATOR;
        Reader readerDictionaryWords = new StringReader(word);
        Reader readerStopWords = new StringReader(word);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(readerDictionaryWords, readerStopWords);
        Set<String> dictionaryWords = Set.of("simona");
        Set<String> stopWords = Set.of("simona;?.,!");
        assertEquals("Dictionary shouldn't contain trailing symbols",
                dictionaryWords, spellChecker.getDictionaryWords());
        assertEquals("Stop-words can contain trailing symbols",
                stopWords, spellChecker.getStopWords());
    }

    @Test
    public void testGetDictionaryAndStopWordsWithMoreWords() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        assertEquals("Testing dictionary words with more words",
                dictionaryWordsFromExample, spellChecker.getDictionaryWords());
        assertEquals("Testing stop-words with more words",
                stopWordsFromExample, spellChecker.getStopWords());
    }

    @Test
    public void testGetDividedWordsByGammaWhenEmpty() {
        Reader reader = new StringReader("");
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(reader, reader);
        assertTrue("Empty words shouldn't divided words by gammas",
                spellChecker.getDividedWordsByGamma().isEmpty());
    }

    @Test
    public void testGetDividedWordsByGammaWordsHelloAndYellow() {
        String text = "";
        text += "hello" + LINE_SEPARATOR;
        text += "yellow" + LINE_SEPARATOR;

        Scale hello = new Scale("hello");
        Scale yellow = new Scale("yellow");
        Map<String, Set<Scale>> dividedWordsByGamma = new HashMap<>();
        dividedWordsByGamma.put("he", Set.of(hello));
        dividedWordsByGamma.put("el", Set.of(hello, yellow));
        dividedWordsByGamma.put("ll", Set.of(hello, yellow));
        dividedWordsByGamma.put("lo", Set.of(hello, yellow));
        dividedWordsByGamma.put("ye", Set.of(yellow));
        dividedWordsByGamma.put("ow", Set.of(yellow));

        Reader reader = new StringReader(text);
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(reader, reader);

        assertEquals(dividedWordsByGamma, spellChecker.getDividedWordsByGamma());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClosestWordsWhenWordIsNull() {
        Reader reader = new StringReader("");
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(reader, reader);
        spellChecker.findClosestWords(null, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindClosestWordsWhenArgumentIsNegative() {
        Reader reader = new StringReader("");
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(reader, reader);
        spellChecker.findClosestWords("hey", -2);
    }

    @Test
    public void testFindClosestWordsWhenWordIsEmpty() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        assertTrue("Empty words shouldn't have close words",
                spellChecker.findClosestWords("", 2).isEmpty());
    }

    @Test
    public void testFindClosestWordsWhenExpectZeroClosestWords() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        assertTrue("There shouldn't be close words when limit is 0",
                spellChecker.findClosestWords("hello", 0).isEmpty());
    }

    @Test
    public void testFindClosestWordsWhenExpectOneExistingWord() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        List<String> closeWords = List.of("hello");
        assertEquals(closeWords, spellChecker.findClosestWords("hello", 1));
    }

    @Test
    public void testFindClosestWordsWhenExpectThreeClosestOfExistingWord() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        List<String> closeWords = List.of("hello", "phello", "chello");
        assertEquals(closeWords, spellChecker.findClosestWords("hello", 3));
    }

    @Test
    public void testFindClosestWordsWhenExpectMoreThanNecessaryClosestOfExistingWord() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        List<String> expectedCloseWords = List.of("zero", "modernistic");
        List<String> actualCloseWords = spellChecker.findClosestWords("zero", 5);
        assertEquals("Expect 2 close words of word zero without 0.0 cosine similarity",
                expectedCloseWords, actualCloseWords.subList(0, 2));
        assertEquals("Expect 5 close words of word zero",
                5, actualCloseWords.size());
        assertTrue("Expect third word to be with cosine similarity 0.0",
                spellChecker.getDictionaryWords().contains(actualCloseWords.get(2)));
        assertTrue("Expect fourth word to be with cosine similarity 0.0",
                spellChecker.getDictionaryWords().contains(actualCloseWords.get(3)));
        assertTrue("Expect fifth word to be with cosine similarity 0.0",
                spellChecker.getDictionaryWords().contains(actualCloseWords.get(4)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMetadataWhenReaderIsNull() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        spellChecker.metadata(null);
    }

    @Test
    public void testMetadataWhenTextIsEmpty() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        Reader emptyTextReader = new StringReader("");
        assertEquals("Metadata information should consist only 0 values when text is empty",
                new Metadata(0, 0, 0), spellChecker.metadata(emptyTextReader));
    }

    @Test
    public void testMetadataFromText() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        assertEquals(new Metadata(69, 8, 7), spellChecker.metadata(testingTextReader));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeWhenReaderIsNull() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        spellChecker.analyze(null, testingTextWriter, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeWhenWriterIsNull() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        spellChecker.analyze(testingTextReader, null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnalyzeWhenSuggestionsCountsAreNegative() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        spellChecker.analyze(testingTextReader, testingTextWriter, -5);
    }

    @Test
    public void testAnalyzeWhenTextIsEmpty() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        Reader emptyTextReader = new StringReader("");
        spellChecker.analyze(emptyTextReader, testingTextWriter, 3);
        String expectedText = LINE_SEPARATOR
                + METADATA_HEADER
                + "0 characters, 0 words, 0 spelling issue(s) found" + LINE_SEPARATOR
                + FINDINGS_HEADER
                + "There are no spelling mistakes";
        assertEquals(expectedText, writer.toString());
    }

    @Test
    public void testAnalyzeText() {
        NaiveSpellChecker spellChecker = new NaiveSpellChecker(dictionaryReaderExample, stopWordsReaderExample);
        spellChecker.analyze(testingTextReader, testingTextWriter, 3);
        String expectedText = testingText + LINE_SEPARATOR
                + "= = = Metadata = = =" + LINE_SEPARATOR
                + "69 characters, 8 words, 7 spelling issue(s) found" + LINE_SEPARATOR
                + "= = = Findings = = =" + LINE_SEPARATOR
                + "Line #1, {melloww} - Possible suggestions are {mellow, yellow, hello}" + LINE_SEPARATOR
                + "Line #1, {iss} - Possible suggestions are {modernistic, abalone, angry}" + LINE_SEPARATOR
                + "Line #1, {yeellow} - Possible suggestions are {yellow, mellow, hello}" + LINE_SEPARATOR
                + "Line #3, {whaat} - Possible suggestions are {what, chocolates, abalone}" + LINE_SEPARATOR
                + "Line #3, {don't} - Possible suggestions are {abalone, balloons, angry}" + LINE_SEPARATOR
                + "Line #3, {be} - Possible suggestions are {balloons, abalone, angry}" + LINE_SEPARATOR
                + "Line #3, {anggrry} - Possible suggestions are {angry, balloons, abalone}" + LINE_SEPARATOR;
        assertEquals(expectedText, writer.toString());
    }
}
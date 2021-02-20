package bg.sofia.uni.fmi.mjt.spellchecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class NaiveSpellChecker implements SpellChecker {
    private static final String STARTS_WITH_NON_ALPHANUMERIC = "^[^a-z0-9]+";
    private static final String ENDS_WITH_NON_ALPHANUMERIC = "[^a-z0-9]+$";
    private static final String STARTS_WITH_WHITESPACES = "^\\s+";
    private static final String ENDS_WITH_WHITESPACES = "\\s+$";
    private static final String BY_WHITESPACES = "\\s+";
    private static final String EMPTY = "";

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String METADATA_HEADER = "= = = Metadata = = =" + LINE_SEPARATOR;
    private static final String FINDINGS_HEADER = "= = = Findings = = =" + LINE_SEPARATOR;

    private static int ROW = 1;
    private static int ALL_CHARACTERS;
    private static int ALL_WORDS;
    private static int ALL_MISTAKES;
    private static int LINE_CHARACTERS;
    private static int LINE_WORDS;
    private static int LINE_MISTAKES;
    /**
     * Stores all wrong words found in current row
     * Key: Row number
     * Value: Set of wrong words
     */
    private Map<Integer, Set<String>> wrongWordsInRow = new TreeMap<>();
    private final Set<String> dictionaryWords = new TreeSet<>();
    private final Set<String> stopWords = new HashSet<>();
    /**
     * Stores all dictionary gammas and scales which contain the gamma
     * Key: Gamma
     * Value: Scale containing the concrete gamma
     */
    private final Map<String, Set<Scale>> dividedWordsByGamma = new HashMap<>();

    /**
     * Creates a new instance of NaiveSpellCheckTool, based on a dictionary of words and stop words
     *
     * @param dictionaryReader a java.io.Reader input stream containing list of dictionary words
     * @param stopwordsReader  a java.io.Reader input stream containing list of stopwords
     */
    public NaiveSpellChecker(Reader dictionaryReader, Reader stopwordsReader) {

        if (dictionaryReader == null) {
            throw new IllegalArgumentException("Cannot read dictionary words from null input stream");
        }
        if (stopwordsReader == null) {
            throw new IllegalArgumentException("Cannot read stopwords from null input stream");
        }

        wrongWordsInRow = new TreeMap<>();
        saveWords(dictionaryReader, WordType.DICTIONARY_WORD);
        saveWords(stopwordsReader, WordType.STOP_WORD);
    }

    private static String removeLeadingTrailingWhitespaces(String word) {

        return word.replaceAll(STARTS_WITH_WHITESPACES, EMPTY)
                .replaceAll(ENDS_WITH_WHITESPACES, EMPTY);
    }

    private static String removeLeadingTrailingSymbols(String word) {

        return word.replaceAll(STARTS_WITH_NON_ALPHANUMERIC, EMPTY)
                .replaceAll(ENDS_WITH_NON_ALPHANUMERIC, EMPTY);
    }

    /**
     * Write text in java.io.Writer output stream
     *
     * @param text   a string that will be written
     * @param output a java.io.Writer output stream where the input string will be written
     */
    private static void write(String text, Writer output) {

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(output);
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Error in writing to output", e);
        }
    }

    private void resetAll() {

        wrongWordsInRow = new TreeMap<>();
        ROW = 1;
        ALL_CHARACTERS = 0;
        ALL_WORDS = 0;
        ALL_MISTAKES = 0;
    }

    private void resetLineValues() {

        LINE_CHARACTERS = 0;
        LINE_WORDS = 0;
        LINE_MISTAKES = 0;
    }

    private void increaseAllValues() {

        ALL_CHARACTERS += LINE_CHARACTERS;
        ALL_WORDS += LINE_WORDS;
        ALL_MISTAKES += LINE_MISTAKES;
        ROW++;
    }

    private void countCharacters(String word) {
        LINE_CHARACTERS += word.length();
    }

    private <S, T> Set<S> update(Map<T, Set<S>> words, T key, S value) {

        Set<S> gammaWords = ((words.containsKey(key)) ? words.get(key) : new LinkedHashSet<>());
        gammaWords.add(value);

        return gammaWords;
    }

    private void putWrongWord(String word) {

        Set<String> wrongWords = update(wrongWordsInRow, ROW, word);
        wrongWordsInRow.put(ROW, wrongWords);
    }

    /**
     * Transfer text from {@code textReader} stream to {@code textBuilder} StringBuilder
     *
     * @param textReader  a java.io.Reader input stream containing some kind of text
     * @param textBuilder a StringBuilder which will receive the input text
     */
    private void transfer(Reader textReader, StringBuilder textBuilder) {

        BufferedReader bufferedReader = new BufferedReader(textReader);
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line += LINE_SEPARATOR;
                textBuilder.append(line);
            }
            if (textBuilder.isEmpty()) {
                textBuilder.append(LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error in reading text", e);
        }
    }

    @Override
    public void analyze(Reader textReader, Writer output, int suggestionsCounts) {

        if (textReader == null) {
            throw new IllegalArgumentException("Cannot analyze: Null input stream");
        }
        if (output == null) {
            throw new IllegalArgumentException("Cannot analyze: Null output stream");
        }
        if (suggestionsCounts < 0) {
            throw new IllegalArgumentException("Cannot analyze: Negative suggestions counts");
        }

        StringBuilder savedTextBuilder = new StringBuilder();
        transfer(textReader, savedTextBuilder);
        write(savedTextBuilder.toString(), output);

        Reader stringReader = new StringReader(savedTextBuilder.toString());
        writeMetadataInformation(stringReader, output);
        writeSuggestionsOfWrongWords(suggestionsCounts, output);
    }

    @Override
    public Metadata metadata(Reader textReader) {

        if (textReader == null) {
            throw new IllegalArgumentException("Cannot read metadata from null input stream");
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(textReader);
            resetAll();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resetLineValues();
                String[] splittedLine = line.split(BY_WHITESPACES);
                for (String word : splittedLine) {
                    increaseLineValues(word);
                }
                increaseAllValues();
            }
            return new Metadata(ALL_CHARACTERS, ALL_WORDS, ALL_MISTAKES);
        } catch (IOException e) {
            throw new IllegalStateException("Error in metadata reading", e);
        }
    }

    @Override
    public List<String> findClosestWords(String word, int n) {

        if (word == null) {
            throw new IllegalArgumentException("Cannot find closest words: Null word is passed");
        }
        if (n < 0) {
            throw new IllegalArgumentException("Cannot find closest words: Negative limit is passed");
        }
        if (word.isEmpty()) {
            return new ArrayList<>();
        }
        if (n == 1 && dictionaryWords.contains(word)) {
            return List.of(word);
        }

        List<String> sortedNeighbours = new ArrayList<>();
        Map<Double, Set<String>> allSimilarSortedWords = getSimilarWordsOf(word);
        allSimilarSortedWords.values().forEach(sortedNeighbours::addAll);

        List<String> closeWords = sortedNeighbours.stream().limit(n).collect(Collectors.toList());
        int numberOfRandom = n - closeWords.size();
        if (numberOfRandom > 0) {
            Set<String> nonSimilarWords = getNonSimilarWordsOf(word, numberOfRandom);
            closeWords.addAll(nonSimilarWords);
        }

        return Collections.unmodifiableList(closeWords);
    }

    /**
     * Add words to dictionary storage if {@code wordType} is connected to dictionary words
     * or add words to stopWords storage if {@code wordType) is connected to stopWords}
     *
     * @param wordsReader a java.io.Reader input stream containing list of words
     * @param wordType    a type of words which {@code wordsReader} input stream contains
     */
    private void saveWords(Reader wordsReader, WordType wordType) {

        try {
            BufferedReader bufferedReader = new BufferedReader(wordsReader);
            String word;
            while ((word = bufferedReader.readLine()) != null) {
                if (wordType.isDictionaryWordType()) {
                    addToDictionary(word);
                } else {
                    addToStopWords(word);
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException("Error in reading words", e);
        }
    }

    /**
     * Loop gammas and connect word to every gamma
     *
     * @param wordScale a Scale containing a word and its gammas
     */
    private void distributeWordByGammas(Scale wordScale) {

        Set<String> wordGammas = wordScale.getGammas().keySet();
        for (String gamma : wordGammas) {
            Set<Scale> wordsWithGamma = update(dividedWordsByGamma, gamma, wordScale);
            dividedWordsByGamma.put(gamma, wordsWithGamma);
        }
    }

    private void addToDictionary(String word) {

        word = word.toLowerCase();
        word = removeLeadingTrailingSymbols(word);
        if (word.length() >= 2) {
            Scale wordScale = new Scale(word);
            dictionaryWords.add(word);
            distributeWordByGammas(wordScale);
        }
    }

    private void addToStopWords(String stopWord) {

        stopWord = stopWord.toLowerCase();
        stopWord = removeLeadingTrailingWhitespaces(stopWord);
        stopWords.add(stopWord);
    }

    public Set<String> getDictionaryWords() {
        return Collections.unmodifiableSet(dictionaryWords);
    }

    public Set<String> getStopWords() {
        return Collections.unmodifiableSet(stopWords);
    }

    public Map<String, Set<Scale>> getDividedWordsByGamma() {
        return Collections.unmodifiableMap(dividedWordsByGamma);
    }

    /**
     * Loop gammas of input {@code word}, find cosine similarity between input {@code word}
     * and every word from every gamma and update similar words of {@code word}
     *
     * @param word a String which will be used to find cosine similarity to appropriate words
     * @return all similar words and their cosine similarities with {@code word}
     */
    private Map<Double, Set<String>> getSimilarWordsOf(String word) {

        Scale wordScale = new Scale(word);
        Set<String> wordGammas = wordScale.getGammas().keySet();
        Map<Double, Set<String>> similarWords = new TreeMap<>(Comparator.reverseOrder());

        for (String gamma : wordGammas) {
            if (dividedWordsByGamma.containsKey(gamma)) {
                for (Scale currentScale : dividedWordsByGamma.get(gamma)) {
                    double cosineSimilarity = currentScale.findCosineSimilarityWith(wordScale);
                    Set<String> updatedNeighbours = update(similarWords, cosineSimilarity, currentScale.getWord());
                    similarWords.put(cosineSimilarity, updatedNeighbours);
                }
            }
        }
        return similarWords;
    }

    /**
     * @param word a String which will be used to find words with 0.0 cosine similarity
     * @param n    a number of words which has 0.0 cosine similarity with {@code word}
     * @return {@code n} non-similar words for {@code word}
     */
    private Set<String> getNonSimilarWordsOf(String word, int n) {

        Scale wordScale = new Scale(word);

        return dictionaryWords.stream()
                .filter(current -> (new Scale(current).findCosineSimilarityWith(wordScale)) == 0.0)
                .limit(n)
                .collect(Collectors.toSet());
    }

    /**
     * Write header of metadata, information of metadata and header of findings
     *
     * @param input  a java.io.Reader input stream which will be used to get metadata information
     * @param output a java.io.Writer output stream where all information will be written
     */
    private void writeMetadataInformation(Reader input, Writer output) {

        Metadata metadataInformation = metadata(input);
        String metadataInformationText = metadataInformation.characters() + " characters, "
                + metadataInformation.words() + " words, "
                + metadataInformation.mistakes() + " spelling issue(s) found" + LINE_SEPARATOR;
        write(METADATA_HEADER, output);
        write(metadataInformationText, output);
        write(FINDINGS_HEADER, output);
    }

    /**
     * Write wrong word and suggestions for every row where wrong word has occurred
     *
     * @param suggestionsCounts a number of suggestions for every wrong word
     * @param output            a java.io.Writer output stream where all information will be written
     */
    private void writeSuggestionsOfWrongWords(int suggestionsCounts, Writer output) {

        if (wrongWordsInRow.isEmpty()) {
            write("There are no spelling mistakes", output);
        }
        for (int currentRow : wrongWordsInRow.keySet()) {
            for (String word : wrongWordsInRow.get(currentRow)) {
                StringBuilder textBuilder = new StringBuilder(
                        "Line #" + currentRow + ", {" + word + "} - Possible suggestions are {");
                List<String> closeWords = findClosestWords(word, suggestionsCounts);
                String separators;
                for (int i = 0; i < closeWords.size(); i++) {
                    separators = ((i != closeWords.size() - 1) ? ", " : "}" + LINE_SEPARATOR);
                    textBuilder.append(closeWords.get(i)).append(separators);
                }
                write(textBuilder.toString(), output);
            }
        }
    }

    private boolean isNotStopWord(String word) {

        return !stopWords.contains(word);
    }

    private boolean isWordNecessary(String word) {

        word = word.toLowerCase();
        if (isNotStopWord(word)) {
            word = removeLeadingTrailingSymbols(word);
            return !word.isEmpty() && isNotStopWord(word);
        }
        return false;
    }

    private void countWord(String word) {

        if (isWordNecessary(word)) {
            LINE_WORDS++;
        }
    }

    private void countMistake(String word) {

        if (isWordNecessary(word)) {
            word = word.toLowerCase();
            word = removeLeadingTrailingSymbols(word);
            if (word.length() >= 2 && !dictionaryWords.contains(word)) {
                putWrongWord(word);
                LINE_MISTAKES++;
            }
        }
    }

    private void increaseLineValues(String word) {

        countCharacters(word);
        countWord(word);
        countMistake(word);
    }
}

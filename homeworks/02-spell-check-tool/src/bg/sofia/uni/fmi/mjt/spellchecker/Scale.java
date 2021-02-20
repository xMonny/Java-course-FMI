package bg.sofia.uni.fmi.mjt.spellchecker;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Scale {

    private final String word;
    private final Map<String, Integer> wordGammas = new LinkedHashMap<>();

    public Scale(String word) {

        if (word == null) {
            throw new IllegalArgumentException("Null word is not accepted");
        }

        this.word = word.toLowerCase();
        setGammas();
    }

    private void addGamma(String gamma) {

        if (wordGammas.containsKey(gamma)) {
            wordGammas.put(gamma, wordGammas.get(gamma) + 1);
        } else {
            wordGammas.put(gamma, 1);
        }
    }

    private void setGammas() {

        String part = "";
        char current;
        for (int i = 0; i < word.length(); i++) {
            current = word.charAt(i);
            part += current;
            if (i != 0) {
                addGamma(part);
                part = part.substring(1);
            }
        }
    }

    public String getWord() {
        return this.word;
    }

    public Map<String, Integer> getGammas() {
        return Collections.unmodifiableMap(wordGammas);
    }

    public double getGammasLength() {

        double length = wordGammas.values().stream()
                .mapToDouble(repeat -> Math.pow(repeat, 2))
                .sum();
        if (length == 0.0) {
            return 0.0;
        }

        return Math.sqrt(length);
    }

    private int sumGammasProduct(Map<String, Integer> one, Map<String, Integer> two) {

        Map<String, Integer> shorter;
        Map<String, Integer> longer;
        if (one.size() <= two.size()) {
            shorter = new LinkedHashMap<>(one);
            longer = new LinkedHashMap<>(two);
        } else {
            shorter = new LinkedHashMap<>(two);
            longer = new LinkedHashMap<>(one);
        }

        return shorter.entrySet().stream()
                .filter(entry -> longer.containsKey(entry.getKey()))
                .mapToInt(entry -> entry.getValue() * longer.get(entry.getKey()))
                .sum();
    }

    public double findCosineSimilarityWith(Scale otherScale) {

        Map<String, Integer> otherGammas = otherScale.getGammas();
        int product = sumGammasProduct(wordGammas, otherGammas);
        double currentGammasLength = getGammasLength();
        double otherGammasLength = otherScale.getGammasLength();

        if (currentGammasLength == 0 || otherGammasLength == 0) {
            throw new ArithmeticException("Dividing to zero is not permitted");
        }

        return product / (currentGammasLength * otherGammasLength);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Scale scale = (Scale) o;
        return word.equals(scale.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}

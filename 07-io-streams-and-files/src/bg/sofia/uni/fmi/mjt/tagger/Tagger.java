package bg.sofia.uni.fmi.mjt.tagger;

import bg.sofia.uni.fmi.mjt.tagger.comparators.PairOccurrencesComparator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tagger {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final Map<String, String> citiesAndCountries;
    private final Map<String, Integer> taggedCities;

    /**
     * Creates a new instance of Tagger for a given list of city/country pairs
     *
     * @param citiesReader a java.io.Reader input stream containing list of cities and countries
     *                     in the specified CSV format
     */
    public Tagger(Reader citiesReader) {
        this.citiesAndCountries = new HashMap<>();
        this.taggedCities = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(citiesReader);
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splittedLine = line.split(",");
                String city = splittedLine[0];
                String country = splittedLine[1];
                citiesAndCountries.put(city, country);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read correctly from file", e);
        }
    }

    public Map<String, String> getCitiesAndCountries() {
        return this.citiesAndCountries;
    }

    public Map<String, Integer> getCitiesAndTags() {
        return this.taggedCities;
    }

    private void markCityAsTagged(String city) {
        if (taggedCities.containsKey(city)) {
            taggedCities.put(city, taggedCities.get(city) + 1);
        } else {
            taggedCities.put(city, 1);
        }
    }

    private void writeInFile(Writer output, String text) {
        try {
            Writer bufferedWriter = new BufferedWriter(output);
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write correctly in file", e);
        }
    }

    /**
     * Processes an input stream of a text file, tags any cities and outputs result
     * to a text output stream.
     *
     * @param text   a java.io.Reader input stream containing text to be processed
     * @param output a java.io.Writer output stream containing the result of tagging
     */
    public void tagCities(Reader text, Writer output) {
        taggedCities.clear();
        BufferedReader bufferedReader = new BufferedReader(text);
        try {
            String line;
            String rememberLine = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().equals("")) {
                    writeInFile(output, LINE_SEPARATOR);
                    continue;
                }
                if (!rememberLine.isEmpty()) {
                    writeInFile(output, LINE_SEPARATOR);
                }
                rememberLine = line;
                for (Map.Entry<String, String> entry : citiesAndCountries.entrySet()) {
                    String city = entry.getKey();
                    String country = entry.getValue();
                    String replacedCity = "<city country=\"" + country + "\">" + city + "</city>";
                    String lineLowerCase = line.toLowerCase();
                    String cityLowerCase = city.toLowerCase();
                    int cityLength = city.length();
                    String filled = "#".repeat(cityLength);
                    if (lineLowerCase.matches(".*[a-z]+" + cityLowerCase + ".*")) {
                        continue;
                    }
                    if (lineLowerCase.matches(".*" + cityLowerCase + "[a-z]+.*")) {
                        continue;
                    }
                    while (lineLowerCase.contains(cityLowerCase)) {
                        int firstIndexCity = lineLowerCase.indexOf(cityLowerCase);
                        int lastIndexCity = firstIndexCity + cityLength;
                        String substringToReplace = line.substring(firstIndexCity, lastIndexCity);
                        rememberLine = rememberLine.replaceFirst(substringToReplace, replacedCity);
                        lineLowerCase = lineLowerCase.replaceFirst(cityLowerCase, filled);

                        markCityAsTagged(city);
                    }
                }
                writeInFile(output, rememberLine);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read correctly from file", e);
        }
    }

    /**
     * Returns a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If @n exceeds the total number of cities tagged, return as many as available
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @param n the maximum number of top tagged cities to return
     * @return a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getNMostTaggedCities(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot get " + n + " most tagged cities");
        }
        List<Pair> pairs = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : taggedCities.entrySet()) {
            pairs.add(new Pair(entry.getKey(), entry.getValue()));
        }
        pairs.sort(new PairOccurrencesComparator());

        List<String> sortedTaggedCities = new ArrayList<>();
        if (n < pairs.size()) {
            for (int i = 0; i < n; i++) {
                sortedTaggedCities.add(pairs.get(i).name());
            }
        } else {
            for (Pair p : pairs) {
                sortedTaggedCities.add(p.name());
            }
        }
        return sortedTaggedCities;
    }

    /**
     * Returns a collection of all tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @return a collection of all tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getAllTaggedCities() {
        return taggedCities.keySet();
    }

    /**
     * Returns the total number of tagged cities in the input text
     * from the last tagCities() invocation
     * In case a particular city has been taged in several occurences, all must be counted.
     * If tagCities() has not been invoked at all, return 0.
     *
     * @return the total number of tagged cities in the input text
     */
    public long getAllTagsCount() {
        int total = 0;
        for (Map.Entry<String, Integer> entry : taggedCities.entrySet()) {
            total += entry.getValue();
        }
        return total;
    }
}
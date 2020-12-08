package bg.sofia.uni.fmi.mjt.tagger;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
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

public class TaggerTest {
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Reader brOneRowCityCountry;
    private Reader brTwoRowsCityCountry;
    private Reader brInputTextCorrectSyntax;
    private Writer bwInputTextCorrectSyntax;
    private Writer writerInputTextCorrectSyntax;

    @Before
    public void prepareForTests() {
        String oneRowCityCountry = "Dubai,United Arab Emirates";
        String twoRowsCityCountry = "Dubai,United Arab Emirates" + LINE_SEPARATOR + "Plovdiv,Bulgaria";

        Reader readerOneRowCityCountry = new StringReader(oneRowCityCountry);
        Reader readerTwoRowsCityCountry = new StringReader(twoRowsCityCountry);

        brOneRowCityCountry = new BufferedReader(readerOneRowCityCountry);
        brTwoRowsCityCountry = new BufferedReader(readerTwoRowsCityCountry);

        String inputTextCorrectSyntax = "";
        inputTextCorrectSyntax += "Plovdiv's largest city" + LINE_SEPARATOR;
        inputTextCorrectSyntax += "in Bulgaria !dubai in UAE ,after plOvDiV" + LINE_SEPARATOR;

        Reader readerInputTextCorrectSyntax = new StringReader(inputTextCorrectSyntax);
        brInputTextCorrectSyntax = new BufferedReader(readerInputTextCorrectSyntax);

        writerInputTextCorrectSyntax = new StringWriter();
        bwInputTextCorrectSyntax = new BufferedWriter(writerInputTextCorrectSyntax);
    }

    @Test
    public void testTaggerWithOneRow() {
        Tagger tagger = new Tagger(brOneRowCityCountry);
        assertEquals(1, tagger.getCitiesAndCountries().size());
        assertTrue(tagger.getCitiesAndCountries().containsKey("Dubai"));
        assertTrue(tagger.getCitiesAndCountries().containsValue("United Arab Emirates"));
    }

    @Test
    public void testTaggerWithTwoRows() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        assertEquals(2, tagger.getCitiesAndCountries().size());
        assertTrue(tagger.getCitiesAndCountries().containsKey("Dubai"));
        assertEquals("United Arab Emirates", tagger.getCitiesAndCountries().get("Dubai"));
        assertTrue(tagger.getCitiesAndCountries().containsKey("Plovdiv"));
        assertEquals("Bulgaria", tagger.getCitiesAndCountries().get("Plovdiv"));
    }

    @Test
    public void testTagCitiesWithoutCities() {
        String inputText = "In this sentence there is no cities " + LINE_SEPARATOR;
        inputText += "so you can't tag :)";
        Reader readerInputText = new StringReader(inputText);
        Reader br = new BufferedReader(readerInputText);

        Writer writer = new StringWriter();
        Writer bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        assertEquals(inputText, writer.toString());
    }

    @Test
    public void testTagCitiesWithPrefix() {
        String inputText = System.lineSeparator() + "Helloplovdiv, I am in thisdubai in UAE";
        Reader readerInputText = new StringReader(inputText);
        Reader br = new BufferedReader(readerInputText);

        Writer writer = new StringWriter();
        Writer bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        assertEquals(inputText, writer.toString());
    }

    @Test
    public void testTagCitiesWithPrefixAndSuffix() {
        String inputText = "helloPlOvDivhello, I am in cityDUbaItown in UAE";
        Reader readerInputText = new StringReader(inputText);
        Reader br = new BufferedReader(readerInputText);

        Writer writer = new StringWriter();
        Writer bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        assertEquals(inputText, writer.toString());
    }

    @Test
    public void testTagCitiesWithSuffix() {
        String inputText = "PlOvDivhello, I am in DUbaItown in UAE";
        Reader readerInputText = new StringReader(inputText);
        Reader br = new BufferedReader(readerInputText);

        Writer writer = new StringWriter();
        Writer bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        assertEquals(inputText, writer.toString());
    }

    @Test
    public void testTagCitiesWithCorrectSyntax() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(brInputTextCorrectSyntax, bwInputTextCorrectSyntax);

        String finalOutput = "<city country=\"Bulgaria\">Plovdiv</city>'s largest city" + LINE_SEPARATOR;
        finalOutput += "in Bulgaria !<city country=\"United Arab Emirates\">Dubai</city> in UAE ";
        finalOutput += ",after <city country=\"Bulgaria\">Plovdiv</city>";

        assertEquals(finalOutput, writerInputTextCorrectSyntax.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNMostTaggedCitiesIllegalArgument() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.getNMostTaggedCities(-5);
    }

    @Test
    public void getNMostTaggedCitiesNoTagged() {
        String inputText = "hPlovdiv's largest city" + LINE_SEPARATOR;
        inputText += "in Bulgaria !hdubai in UAE ,after mplOvDiV";

        Reader readerInputText = new StringReader(inputText);
        Reader br = new BufferedReader(readerInputText);

        Writer writer = new StringWriter();
        Writer bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        assertEquals(0, tagger.getNMostTaggedCities(5).size());
    }

    @Test
    public void getNMostTaggedCitiesNLessThanSize() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(brInputTextCorrectSyntax, bwInputTextCorrectSyntax);

        assertEquals(1, tagger.getNMostTaggedCities(1).size());
        assertTrue(tagger.getNMostTaggedCities(1).contains("Plovdiv"));
    }

    @Test
    public void getNMostTaggedCitiesNEqualsSize() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(brInputTextCorrectSyntax, bwInputTextCorrectSyntax);

        List<String> taggedCities = List.of("Plovdiv", "Dubai");

        assertEquals(2, tagger.getNMostTaggedCities(2).size());
        assertEquals(taggedCities, tagger.getNMostTaggedCities(2));
    }

    @Test
    public void getNMostTaggedCitiesNMoreThanSize() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(brInputTextCorrectSyntax, bwInputTextCorrectSyntax);

        List<String> taggedCities = List.of("Plovdiv", "Dubai");

        assertEquals(2, tagger.getNMostTaggedCities(10).size());
        assertEquals(taggedCities, tagger.getNMostTaggedCities(10));
    }

    @Test
    public void getNMostTaggedCitiesChangeSortOrder() {
        String inputText = "largest city duBAI and plovDIv" + LINE_SEPARATOR;
        inputText += "!dubai in UAE pLoVDiV,after duBAI plovdiV";

        Reader readerInputText = new StringReader(inputText);
        Reader br = new BufferedReader(readerInputText);

        Writer writer = new StringWriter();
        Writer bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        List<String> taggedCities = List.of("Dubai", "Plovdiv");

        assertEquals(2, tagger.getNMostTaggedCities(10).size());
        assertEquals(taggedCities, tagger.getNMostTaggedCities(10));
    }

    @Test
    public void testGetAllTaggedCitiesWhenEmpty() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);

        assertEquals(0, tagger.getAllTaggedCities().size());
    }

    @Test
    public void testGetAllTaggedCitiesWithOccurrences() {
        String inputText = "Plovdiv's largest city" + LINE_SEPARATOR;
        inputText += "!dubai in UAE ,after plOvDiV";

        Reader readerInputText = new StringReader(inputText);
        Reader br = new BufferedReader(readerInputText);

        Writer writer = new StringWriter();
        Writer bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        Map<String, Integer> pairCityOccurrences = new HashMap<>();
        pairCityOccurrences.put("Plovdiv", 2);
        pairCityOccurrences.put("Dubai", 1);

        assertEquals(pairCityOccurrences, tagger.getCitiesAndTags());
    }

    @Test
    public void testTagCitiesClear() {
        String inputText = "Plovdiv's largest city" + LINE_SEPARATOR;
        inputText += "!dubai in UAE ,after plOvDiV";

        Reader readerInputText;
        readerInputText = new StringReader(inputText);
        Reader br;
        br = new BufferedReader(readerInputText);

        Writer writer;
        writer = new StringWriter();
        Writer bw;
        bw = new BufferedWriter(writer);

        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(br, bw);

        Map<String, Integer> pairCityOccurrences = new HashMap<>();
        pairCityOccurrences.put("Plovdiv", 2);
        pairCityOccurrences.put("Dubai", 1);

        readerInputText = new StringReader(inputText);
        br = new BufferedReader(readerInputText);
        writer = new StringWriter();
        bw = new BufferedWriter(writer);

        tagger.tagCities(br, bw);

        assertEquals(pairCityOccurrences, tagger.getCitiesAndTags());
    }

    @Test
    public void testGetAllTaggedCities() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(brInputTextCorrectSyntax, bwInputTextCorrectSyntax);

        Set<String> taggedCities = Set.of("Plovdiv", "Dubai");

        assertEquals(2, tagger.getAllTaggedCities().size());
        assertEquals(taggedCities, tagger.getAllTaggedCities());
    }

    @Test
    public void testGetAllTagsCountWhenEmpty() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);

        assertEquals(0, tagger.getAllTagsCount());
    }

    @Test
    public void testGetAllTagsCount() {
        Tagger tagger = new Tagger(brTwoRowsCityCountry);
        tagger.tagCities(brInputTextCorrectSyntax, bwInputTextCorrectSyntax);

        assertEquals(3, tagger.getAllTagsCount());
    }
}

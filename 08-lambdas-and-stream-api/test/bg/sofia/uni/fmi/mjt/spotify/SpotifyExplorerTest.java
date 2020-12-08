package bg.sofia.uni.fmi.mjt.spotify;

import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpotifyExplorerTest {

    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String informationRow = "id,artists,name,year,popularity,duration_ms,tempo,loudness,valence,"
            + "acousticness,danceability,energy,liveness,speechiness,explicit";
    private static final String artistNameAmerican = "James Man";
    private static final String artistNameFrench = "Fortugé";
    private static final String artistNameBulgarian = "Симона";
    private static final Set<String> namesAmerican = Set.of(artistNameAmerican);
    private static final Set<String> namesFrench = Set.of(artistNameFrench);
    private static final Set<String> namesBulgarian = Set.of(artistNameBulgarian);
    private static final Set<String> namesAmericanFrench = Set.of(artistNameAmerican, artistNameFrench);
    private static final Set<String> namesAmericanBulgarian = Set.of(artistNameAmerican, artistNameBulgarian);
    private static final Set<String> namesFrenchBulgarian = Set.of(artistNameFrench, artistNameBulgarian);
    private static final Set<String> namesAmericanBulgarianFrench = Set.of(artistNameAmerican,
            artistNameBulgarian,
            artistNameFrench);

    private static final String lineBulgarianYear2019Duration100ExplicitFalse = "1x,['Симона'],"
            + "NameHere,2019,0,6000000,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0";
    private static final String lineAmericanBulgarianYear2000Duration30ExplicitTrue = "2x,['James Man'; 'Симона'],"
            + "NameHere,2000,0,1800000,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1";
    private static final String lineAmericanBulgarianFrenchYear2005Duration50ExplicitTrue = "3x,"
            + "['James Man'; 'Симона'; 'Fortugé'],NameHere,2005,0,3000000,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1";
    private static final String lineAmericanBulgarianFrenchYear2006Duration50ExplicitTrue = "3x,"
            + "['James Man'; 'Симона'; 'Fortugé'],NameHere,2006,0,3000000,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1";

    public static final SpotifyTrack trackBulgarianYear2019Duration100ExplicitFalse = new SpotifyTrack("1x",
            namesBulgarian, "NameHere", 2019, 0, 6000000, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);
    public static final SpotifyTrack trackAmericanBulgarianYear2000Duration30ExplicitTrue = new SpotifyTrack("2x",
            namesAmericanBulgarian, "NameHere", 2000, 0, 1800000, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true);
    public static final SpotifyTrack trackAmericanBulgarianFrenchYear2005Duration50ExplicitTrue = new SpotifyTrack("3x",
            namesAmericanBulgarianFrench, "NameHere", 2005, 0, 3000000, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true);
    public static final SpotifyTrack trackAmericanBulgarianFrenchYear2006Duration50ExplicitTrue = new SpotifyTrack("3x",
            namesAmericanBulgarianFrench, "NameHere", 2006, 0, 3000000, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true);

    private static final String lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue = "4x,"
            + "['Симона'],NameHere,1980,0,0,0.0,100.1,0.51,0.0,0.0,0.0,0.0,0.0,1";
    private static final String lineAmericanBulgarianYear1985PositiveValenceExplicitFalse = "5x,"
            + "['James Man'; 'Симона'],NameHere,1985,0,0,0.0,0.0,0.29,0.0,0.0,0.0,0.0,0.0,0";
    private static final String lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse = "6x,"
            + "['James Man'; 'Симона'],NameHere,1980,0,0,0.0,50.2,-0.3,0.0,0.0,0.0,0.0,0.0,0";
    private static final String lineAmericanBulgarianYear1980NegativeValenceExplicitFalse = "6x,"
            + "['James Man'; 'Симона'],NameHere,1980,0,0,0.0,0.0,-0.3,0.0,0.0,0.0,0.0,0.0,0";

    private static final SpotifyTrack trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue = new SpotifyTrack(
            "4x", namesBulgarian, "NameHere", 1980, 0, 0, 0.0, 100.1,
            0.51, 0.0, 0.0, 0.0, 0.0, 0.0, true);
    private static final SpotifyTrack trackAmericanBulgarianYear1985PositiveValenceExplicitFalse = new SpotifyTrack(
            "5x", namesAmericanBulgarian, "NameHere", 1985, 0, 0, 0.0, 0.0,
            0.29, 0.0, 0.0, 0.0, 0.0, 0.0, false);
    private static final SpotifyTrack trackAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse = new
            SpotifyTrack("6x", namesAmericanBulgarian, "NameHere", 1980, 0, 0, 0.0, 50.2,
            -0.3, 0.0, 0.0, 0.0, 0.0, 0.0, false);
    private static final SpotifyTrack trackAmericanBulgarianYear1980NegativeValenceExplicitFalse = new
            SpotifyTrack("6x", namesAmericanBulgarian, "NameHere", 1980, 0, 0, 0.0, 0.0,
            -0.3, 0.0, 0.0, 0.0, 0.0, 0.0, false);

    private static final String lineAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse = "7x,"
            + "['James Man'; 'Симона'; 'Fortugé'],NameHere,1991,99,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0";
    private static final String lineAmericanBulgarianFrenchYear1999Popularity51ExplicitFalse = "8x,"
            + "['James Man'; 'Симона'; 'Fortugé'],NameHere,1999,51,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0";
    private static final String lineAmericanBulgarianFrenchYear1992Popularity51ExplicitFalse = "9x,"
            + "['James Man'; 'Симона'; 'Fortugé'],NameHere,1992,51,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0";

    private static final SpotifyTrack trackAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse = new SpotifyTrack(
            "7x", namesAmericanBulgarianFrench, "NameHere", 1991, 99, 0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);
    private static final SpotifyTrack trackAmericanBulgarianFrenchYear1999Popularity51ExplicitFalse = new SpotifyTrack(
            "8x", namesAmericanBulgarianFrench, "NameHere", 1999, 51, 0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);
    private static final SpotifyTrack trackAmericanBulgarianFrenchYear1992Popularity51ExplicitFalse = new SpotifyTrack(
            "9x", namesAmericanBulgarianFrench, "NameHere", 1992, 51, 0, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false);

    private static String textWithInformationLine;
    private static Reader informationRowReader;

    @Before
    public void prepareForTest() {
        textWithInformationLine = informationRow + LINE_SEPARATOR;
        informationRowReader = new StringReader(informationRow);
    }

    @Test
    public void testGetAllSpotifyTracksWithNoTracks() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        assertTrue(explorer.getAllSpotifyTracks().isEmpty());
    }

    @Test
    public void testGetAllSpotifyTracksWithTracks() {
        textWithInformationLine += lineAmericanBulgarianFrenchYear2005Duration50ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Collection<SpotifyTrack> spotifyTracks = List.of(trackAmericanBulgarianFrenchYear2005Duration50ExplicitTrue,
                trackAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse,
                trackBulgarianYear2019Duration100ExplicitFalse);
        assertEquals(spotifyTracks, explorer.getAllSpotifyTracks());
    }

    @Test
    public void testGetExplicitSpotifyTracksWhenEmpty() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        assertTrue(explorer.getExplicitSpotifyTracks().isEmpty());
    }

    @Test
    public void testGetExplicitSpotifyTracksWhenHaveTrueAndFalseExplicit() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Collection<SpotifyTrack> explicitSpotifyTracks;
        explicitSpotifyTracks = List.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue);
        assertEquals(explicitSpotifyTracks, explorer.getExplicitSpotifyTracks());
    }

    @Test
    public void testGetExplicitSpotifyTracksWhenHaveOnlyFalseExplicit() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertTrue(explorer.getExplicitSpotifyTracks().isEmpty());
    }

    @Test
    public void testGetExplicitSpotifyTracksWhenHaveOnlyTrueExplicit() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear2000Duration30ExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Collection<SpotifyTrack> explicitSpotifyTracks;
        explicitSpotifyTracks = List.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue,
                trackAmericanBulgarianYear2000Duration30ExplicitTrue);
        assertEquals(explicitSpotifyTracks, explorer.getExplicitSpotifyTracks());
    }

    @Test
    public void testGroupSpotifyTracksByYearWhenEmpty() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        assertTrue(explorer.groupSpotifyTracksByYear().isEmpty());
    }

    @Test
    public void testGroupSpotifyTracksByYearTwoDifferentTracksInOneYear() {
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Map<Integer, Set<SpotifyTrack>> tracksGroupedByYear = Map.of(
                1980, Set.of(trackAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse,
                        trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue));
        assertEquals(tracksGroupedByYear, explorer.groupSpotifyTracksByYear());
    }

    @Test
    public void testGroupSpotifyTracksByYearTwoTracksWithSameIDInOneYear() {
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear1980NegativeValenceExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Map<Integer, Set<SpotifyTrack>> tracksGroupedByYear = Map.of(
                1980, Set.of(trackAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse));
        assertEquals(tracksGroupedByYear, explorer.groupSpotifyTracksByYear());
    }

    @Test
    public void testGroupSpotifyTracksByYearTwoDifferentTracksInDifferentYears() {
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Map<Integer, Set<SpotifyTrack>> tracksGroupedByYear = Map.of(
                1980, Set.of(trackAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse),
                2019, Set.of(trackBulgarianYear2019Duration100ExplicitFalse));
        assertEquals(tracksGroupedByYear, explorer.groupSpotifyTracksByYear());
    }

    @Test
    public void testGroupSpotifyTracksByYearTwoSameTracksInDifferentYears() {
        textWithInformationLine += lineAmericanBulgarianFrenchYear2005Duration50ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear2006Duration50ExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Map<Integer, Set<SpotifyTrack>> tracksGroupedByYear = Map.of(
                2005, Set.of(trackAmericanBulgarianFrenchYear2005Duration50ExplicitTrue),
                2006, Set.of(trackAmericanBulgarianFrenchYear2006Duration50ExplicitTrue));
        assertEquals(tracksGroupedByYear, explorer.groupSpotifyTracksByYear());
    }

    @Test
    public void testGroupSpotifyTracksByYearMoreTracks() {
        textWithInformationLine += lineAmericanBulgarianFrenchYear2005Duration50ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear2006Duration50ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Map<Integer, Set<SpotifyTrack>> tracksGroupedByYear = Map.of(
                2005, Set.of(trackAmericanBulgarianFrenchYear2005Duration50ExplicitTrue),
                2006, Set.of(trackAmericanBulgarianFrenchYear2006Duration50ExplicitTrue),
                1980, Set.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue,
                        trackAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse));
        assertEquals(tracksGroupedByYear, explorer.groupSpotifyTracksByYear());
    }

    @Test
    public void testGetArtistActiveYearsWhenEmpty() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        assertEquals(0, explorer.getArtistActiveYears(artistNameBulgarian));
    }

    @Test
    public void testGetArtistActiveYearsWhenNameIsNotFound() {
        textWithInformationLine += lineAmericanBulgarianYear2000Duration30ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(0, explorer.getArtistActiveYears(artistNameFrench));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetArtistActiveYearsNullArgument() {
        textWithInformationLine += lineAmericanBulgarianYear2000Duration30ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        explorer.getArtistActiveYears(null);
    }

    @Test
    public void testGetArtistActiveYearsWhenHaveOnlyOneYearOnce() {
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(1, explorer.getArtistActiveYears(artistNameBulgarian));
    }

    @Test
    public void testGetArtistActiveYearsWhenHaveOnlyOneYearTwice() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(1, explorer.getArtistActiveYears(artistNameBulgarian));
    }

    @Test
    public void testGetArtistActiveYearsWhenHaveMaxAndMinYear() {
        textWithInformationLine += lineAmericanBulgarianYear2000Duration30ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(21, explorer.getArtistActiveYears(artistNameBulgarian));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopNHighestValenceTracksFromThe80sNullArgument() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        explorer.getTopNHighestValenceTracksFromThe80s(-5);
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sWhenNoTracks() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        assertTrue(explorer.getTopNHighestValenceTracksFromThe80s(3).isEmpty());
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sWhenYearIsNotIn80s() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertTrue(explorer.getTopNHighestValenceTracksFromThe80s(1).isEmpty());
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sNegativeValence() {
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertTrue(explorer.getTopNHighestValenceTracksFromThe80s(1).isEmpty());
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sOnePositiveValenceTOP0() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertTrue(explorer.getTopNHighestValenceTracksFromThe80s(0).isEmpty());
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sOnePositiveValenceTOP1() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        List<SpotifyTrack> tracksIn80sWithPositiveValence;
        tracksIn80sWithPositiveValence = List.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue);
        assertEquals(tracksIn80sWithPositiveValence, explorer.getTopNHighestValenceTracksFromThe80s(1));
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sOnePositiveValenceTOP5() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        List<SpotifyTrack> tracksIn80sWithPositiveValence;
        tracksIn80sWithPositiveValence = List.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue);
        assertEquals(tracksIn80sWithPositiveValence, explorer.getTopNHighestValenceTracksFromThe80s(5));
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sTwoPositiveOneNegativeValenceTOP0() {
        textWithInformationLine += lineAmericanBulgarianYear1985PositiveValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;

        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertTrue(explorer.getTopNHighestValenceTracksFromThe80s(0).isEmpty());
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sTwoPositiveOneNegativeValenceTOP1() {
        textWithInformationLine += lineAmericanBulgarianYear1985PositiveValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;

        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        List<SpotifyTrack> tracksIn80sWithPositiveValence;
        tracksIn80sWithPositiveValence = List.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue);
        assertEquals(tracksIn80sWithPositiveValence, explorer.getTopNHighestValenceTracksFromThe80s(1));
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sTwoPositiveOneNegativeValenceTOP2() {
        textWithInformationLine += lineAmericanBulgarianYear1985PositiveValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;

        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        List<SpotifyTrack> tracksIn80sWithPositiveValence;
        tracksIn80sWithPositiveValence = List.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue,
                trackAmericanBulgarianYear1985PositiveValenceExplicitFalse);
        assertEquals(tracksIn80sWithPositiveValence, explorer.getTopNHighestValenceTracksFromThe80s(2));
    }

    @Test
    public void testGetTopNHighestValenceTracksFromThe80sTwoPositiveOneNegativeValenceTOP5() {
        textWithInformationLine += lineAmericanBulgarianYear1985PositiveValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;

        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        List<SpotifyTrack> tracksIn80sWithPositiveValence;
        tracksIn80sWithPositiveValence = List.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue,
                trackAmericanBulgarianYear1985PositiveValenceExplicitFalse);
        assertEquals(tracksIn80sWithPositiveValence, explorer.getTopNHighestValenceTracksFromThe80s(5));
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetMostPopularTrackFromThe90sWhenEmpty() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        explorer.getMostPopularTrackFromThe90s();
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetMostPopularTrackFromThe90sWhenOutOfYear() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        explorer.getMostPopularTrackFromThe90s();
    }

    @Test
    public void testGetMostPopularTrackFromThe90sWithOneTrack() {
        textWithInformationLine += lineAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(trackAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse,
                explorer.getMostPopularTrackFromThe90s());
    }

    @Test
    public void testGetMostPopularTrackFromThe90sWithTwoTracksWithEqualPopularity() {
        textWithInformationLine += lineAmericanBulgarianFrenchYear1992Popularity51ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear1999Popularity51ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals("Return any of them when have equal popularity",
                trackAmericanBulgarianFrenchYear1992Popularity51ExplicitFalse,
                explorer.getMostPopularTrackFromThe90s());
    }

    @Test
    public void testGetMostPopularTrackFromThe90sWithTwoTracksWithDifferentPopularity() {
        textWithInformationLine += lineAmericanBulgarianFrenchYear1999Popularity51ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(trackAmericanBulgarianFrenchYear1991Popularity99ExplicitFalse,
                explorer.getMostPopularTrackFromThe90s());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumberOfLongerTracksBeforeYearWhenMinutesAreNegative() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        explorer.getNumberOfLongerTracksBeforeYear(-100, 2019);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumberOfLongerTracksBeforeYearWhenYearIsNegative() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        explorer.getNumberOfLongerTracksBeforeYear(100, -2019);
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearWhenEmpty() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        assertEquals(0, explorer.getNumberOfLongerTracksBeforeYear(100, 2019));
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearWhenTrackYearIsOutOfRange() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(0, explorer.getNumberOfLongerTracksBeforeYear(100, 2000));
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearWhenTrackMinutesAreOutOfRange() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(0, explorer.getNumberOfLongerTracksBeforeYear(101, 2020));
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearWhenTrackYearAndMinutesAreInRangeOneTrack() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(1, explorer.getNumberOfLongerTracksBeforeYear(99, 2020));
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearWhenTrackYearAndMinutesAreInRangeAndMinutesOutOfRangeTwoTracks() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear2000Duration30ExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(1, explorer.getNumberOfLongerTracksBeforeYear(50, 2020));
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearWhenTrackYearAndMinutesAreInRangeAndYearOutOfRangeTwoTracks() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear2000Duration30ExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(1, explorer.getNumberOfLongerTracksBeforeYear(29, 2018));
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearWhenTrackYearAndMinutesAreInRangeTwoTracks() {
        textWithInformationLine += lineBulgarianYear2019Duration100ExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianYear2000Duration30ExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(2, explorer.getNumberOfLongerTracksBeforeYear(29, 2020));
    }

    @Test
    public void testGetNumberOfLongerTracksBeforeYearTracksWithSameIDButDifferentYears() {
        textWithInformationLine += lineAmericanBulgarianFrenchYear2005Duration50ExplicitTrue + LINE_SEPARATOR;
        textWithInformationLine += lineAmericanBulgarianFrenchYear2006Duration50ExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertEquals(2, explorer.getNumberOfLongerTracksBeforeYear(29, 2010));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTheLoudestTrackInYearWhenYearNegative() {
        SpotifyExplorer explorer = new SpotifyExplorer(informationRowReader);
        explorer.getTheLoudestTrackInYear(-100);
    }

    @Test
    public void testGetTheLoudestTrackInYearWhenYearNotExists() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        assertTrue(explorer.getTheLoudestTrackInYear(2000).isEmpty());
    }

    @Test
    public void testGetTheLoudestTrackInYearWhenYearExistsOneTrack() {
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Optional<SpotifyTrack> optional = Optional.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue);
        assertEquals(optional, explorer.getTheLoudestTrackInYear(1980));
    }

    @Test
    public void testGetTheLoudestTrackInYearWhenYearExistsTwoTracks() {
        textWithInformationLine += lineAmericanBulgarianYear1980Loudness50NegativeValenceExplicitFalse + LINE_SEPARATOR;
        textWithInformationLine += lineBulgarianYear1980Loudness100PositiveValenceExplicitTrue;
        Reader readerTextWithInformationLine = new StringReader(textWithInformationLine);
        SpotifyExplorer explorer = new SpotifyExplorer(readerTextWithInformationLine);
        Optional<SpotifyTrack> optional = Optional.of(trackBulgarianYear1980Loudness100PositiveValenceExplicitTrue);
        assertEquals(optional, explorer.getTheLoudestTrackInYear(1980));
    }
}

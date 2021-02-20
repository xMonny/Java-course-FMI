package bg.sofia.uni.fmi.mjt.spellchecker;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ScaleTest {
    private static final double DELTA = 1e-10;

    private static final Scale EMPTY = new Scale("");
    private static final Scale S = new Scale("s");
    private static final Scale SI = new Scale("si");
    private static final Scale HE = new Scale("hE");
    private static final Scale HELLO = new Scale("hello");
    private static final Scale HELL = new Scale("hell");
    private static final Scale PHELLO = new Scale("phello");
    private static final Scale HELLLO = new Scale("Helllo");
    private static final Scale HELO = new Scale("helO");
    private static final Scale LOHE = new Scale("loHE");
    private static final Scale CAT = new Scale("CAT");
    private static final Scale BONBON = new Scale("bonBON");
    private static final Scale ALABALA = new Scale("AlaBaLa");
    private static final Scale LABELITY = new Scale("LaBeLiTy");
    private static final Scale ABALONE = new Scale("abalone");
    private static final Scale BALLOONS = new Scale("baLLoons");

    private static final double EMPTY_LENGTH = 0.0;
    private static final double HELLO_LENGTH = 2.0;
    private static final double HELLLO_LENGTH = 2.6457513111;
    private static final double HELL_LENGTH = 1.7320508075;
    private static final double BONBON_LENGTH = 3.0;
    private static final double ALABALA_LENGTH = 3.1622776602;
    private static final double LABELITY_LENGTH = 2.6457513111;

    private static final double SIMILARITY_0 = 0.0;
    private static final double SIMILARITY_1 = 1.0;
    private static final double HELLO_HE_SIMILARITY = 0.5;
    private static final double HELLO_HELLLO_SIMILARITY = 0.9449111825;
    private static final double HELL_HELLLO_SIMILARITY = 0.87287156094;
    private static final double HELLO_PHELLO_SIMILARITY = 0.894427191;
    private static final double HELO_LOHE_SIMILARITY = 0.6666666667;
    private static final double ALABALA_LABELITY_SIMILARITY = 0.3585685828;
    private static final double ABALONE_BALLOONS_SIMILARITY = 0.6172133998;

    private Map<String, Integer> gammas;

    @Before
    public void prepareForGetGammaLengthMethods() {
        gammas = new LinkedHashMap<>();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScaleIllegalArgument() {
        new Scale(null);
    }

    @Test
    public void testGetGammasFromEmptyWord() {
        assertTrue("Empty words shouldn't have gammas",
                EMPTY.getGammas().isEmpty());
    }

    @Test
    public void testGetGammasFromOneLetter() {
        assertTrue("One-lettered words shouldn't have gammas",
                S.getGammas().isEmpty());
    }

    @Test
    public void testGetGammasFromTwoLetters() {
        gammas.put("si", 1);
        assertEquals("Two-lettered words should have 1 gamma which is equal to word",
                gammas, SI.getGammas());
    }

    @Test
    public void testGetGammasFromLowerCaseWordHello() {
        gammas.put("he", 1);
        gammas.put("el", 1);
        gammas.put("ll", 1);
        gammas.put("lo", 1);
        assertEquals(gammas, HELLO.getGammas());
    }

    @Test
    public void testGetGammasFromUpperCaseWordCAT() {
        gammas.put("ca", 1);
        gammas.put("at", 1);
        assertEquals(gammas, CAT.getGammas());
    }

    @Test
    public void testGetGammasFromWordBonbon() {
        gammas.put("bo", 2);
        gammas.put("on", 2);
        gammas.put("nb", 1);
        assertEquals(gammas, BONBON.getGammas());
    }

    @Test
    public void testGetGammasFromWordAlabala() {
        gammas.put("al", 2);
        gammas.put("la", 2);
        gammas.put("ab", 1);
        gammas.put("ba", 1);

        assertEquals(gammas, ALABALA.getGammas());
    }

    @Test
    public void testGetGammasFromWordLabelity() {
        gammas.put("la", 1);
        gammas.put("ab", 1);
        gammas.put("be", 1);
        gammas.put("el", 1);
        gammas.put("li", 1);
        gammas.put("it", 1);
        gammas.put("ty", 1);

        assertEquals(gammas, LABELITY.getGammas());
    }

    @Test
    public void testGetGammasLengthFromEmptyWord() {
        assertEquals("Empty words should have length 0",
                EMPTY_LENGTH, EMPTY.getGammasLength(), DELTA);
    }

    @Test
    public void testGetGammasLengthFromWordHello() {
        assertEquals(HELLO_LENGTH, HELLO.getGammasLength(), DELTA);
    }

    @Test
    public void testGetGammasLengthFromWordHell() {
        assertEquals(HELL_LENGTH, HELL.getGammasLength(), DELTA);
    }

    @Test
    public void testGetGammasLengthFromWordBonbon() {
        assertEquals(BONBON_LENGTH, BONBON.getGammasLength(), DELTA);
    }

    @Test
    public void testGetGammasLengthFromWordHelllo() {
        assertEquals(HELLLO_LENGTH, HELLLO.getGammasLength(), DELTA);
    }

    @Test
    public void testGetGammasLengthFromWordAlabala() {
        assertEquals(ALABALA_LENGTH, ALABALA.getGammasLength(), DELTA);
    }

    @Test
    public void testGetGammasLengthFromWordLabelity() {
        assertEquals(LABELITY_LENGTH, LABELITY.getGammasLength(), DELTA);
    }

    @Test(expected = ArithmeticException.class)
    public void testFindCosineSimilarityBetweenEmptyWordAndHello() {
        EMPTY.findCosineSimilarityWith(HELLO);
    }

    @Test(expected = ArithmeticException.class)
    public void testFindCosineSimilarityBetweenHelloAndEmptyWord() {
        HELLO.findCosineSimilarityWith(EMPTY);
    }

    @Test
    public void testFindCosineSimilarityWithOneWord() {
        assertEquals(SIMILARITY_1,
                CAT.findCosineSimilarityWith(CAT), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithTwoDifferentWords() {
        assertEquals(SIMILARITY_0, HELLO.findCosineSimilarityWith(CAT), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithFirstShortSecondLong() {
        assertEquals(HELLO_HE_SIMILARITY, HE.findCosineSimilarityWith(HELLO), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithFirstLongSecondShort() {
        assertEquals(HELLO_HE_SIMILARITY, HELLO.findCosineSimilarityWith(HE), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordsHelloAndHelllo() {
        assertEquals(HELLO_HELLLO_SIMILARITY, HELLO.findCosineSimilarityWith(HELLLO), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordsHellloAndHello() {
        assertEquals(HELLO_HELLLO_SIMILARITY, HELLLO.findCosineSimilarityWith(HELLO), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordsHellAndHelllo() {
        assertEquals(HELL_HELLLO_SIMILARITY, HELL.findCosineSimilarityWith(HELLLO), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordHelloTwice() {
        assertEquals(SIMILARITY_1, HELLO.findCosineSimilarityWith(HELLO), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordHellloTwice() {
        assertEquals(SIMILARITY_1, HELLLO.findCosineSimilarityWith(HELLLO), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordsHelloAndPhello() {
        assertEquals(HELLO_PHELLO_SIMILARITY, HELLO.findCosineSimilarityWith(PHELLO), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordsHeloAndLohe() {
        assertEquals(HELO_LOHE_SIMILARITY, HELO.findCosineSimilarityWith(LOHE), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordsAlabalaAndLabelity() {
        assertEquals(ALABALA_LABELITY_SIMILARITY, ALABALA.findCosineSimilarityWith(LABELITY), DELTA);
    }

    @Test
    public void testFindCosineSimilarityWithWordsAbaloneAndBalloons() {
        assertEquals(ABALONE_BALLOONS_SIMILARITY, ABALONE.findCosineSimilarityWith(BALLOONS), DELTA);
    }

    @Test
    public void testScaleHelloEqualsScaleHello() {
        assertEquals(HELLO, HELLO);
    }

    @Test
    public void testScaleHelloNotEqualsScaleCat() {
        assertNotEquals(HELLO, CAT);
    }

    @Test
    public void testScaleHelloNotEqualsNull() {
        assertNotEquals(HELLO, null);
    }
}

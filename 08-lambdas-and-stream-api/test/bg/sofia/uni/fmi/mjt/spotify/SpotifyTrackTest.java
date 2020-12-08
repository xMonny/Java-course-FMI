package bg.sofia.uni.fmi.mjt.spotify;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Set;

public class SpotifyTrackTest {
    private static final String artistNameAmerican = "James Man";
    private static final String artistNameFrench = "Fortugé";
    private static final String artistNameBulgarian = "Симона";

    private static final Set<String> namesAmericanBulgarianFrench = Set.of(artistNameAmerican,
            artistNameBulgarian,
            artistNameFrench);

    private static final String lineAmericanBulgarianFrenchYear2005Duration50ExplicitTrue = "3x,"
            + "['James Man'; 'Симона'; 'Fortugé'],NameHere,2005,0,50,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1";

    public static final SpotifyTrack trackAmericanBulgarianFrenchYear2005Duration50ExplicitTrue = new SpotifyTrack("3x",
            namesAmericanBulgarianFrench, "NameHere", 2005, 0, 50, 0.0, 0.0,
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true);

    @Test
    public void testOf() {
        assertEquals(trackAmericanBulgarianFrenchYear2005Duration50ExplicitTrue,
                SpotifyTrack.of(lineAmericanBulgarianFrenchYear2005Duration50ExplicitTrue));
    }
}

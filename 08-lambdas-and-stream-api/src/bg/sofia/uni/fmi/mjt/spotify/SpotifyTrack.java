package bg.sofia.uni.fmi.mjt.spotify;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public record SpotifyTrack(String id, Set<String> artists, String name, int year, int popularity, long duration_ms,
                           double tempo, double loudness, double valence, double acousticness, double danceability,
                           double energy, double liveness, double speechiness, boolean explicit) {

    private static final String DELIMITER_FOR_LINE = ",";
    private static final String DELIMITER_FOR_ARTISTS = "; ";

    private static Set<String> getArtistsNames(String text) {
        Set<String> allArtistsNames = new HashSet<>();
        String[] splittedArtistsNames = text.split(DELIMITER_FOR_ARTISTS);
        for (int i = 0; i < splittedArtistsNames.length; i++) {
            String current = splittedArtistsNames[i];
            String currentArtistName = current.replaceAll("\\[", "")
                    .replaceAll("\\]", "")
                    .replaceAll("'", "");
            allArtistsNames.add(currentArtistName);
        }
        return allArtistsNames;
    }

    public static SpotifyTrack of(String line) {
        String[] tokens = line.split(DELIMITER_FOR_LINE);

        String id = tokens[0];
        Set<String> artists = getArtistsNames(tokens[1]);
        String name = tokens[2];
        int year = Integer.parseInt(tokens[3]);
        int popularity = Integer.parseInt(tokens[4]);
        long duration = Long.parseLong(tokens[5]);
        double tempo = Double.parseDouble(tokens[6]);
        double loudness = Double.parseDouble(tokens[7]);
        double valence = Double.parseDouble(tokens[8]);
        double acousticness = Double.parseDouble(tokens[9]);
        double danceability = Double.parseDouble(tokens[10]);
        double energy = Double.parseDouble(tokens[11]);
        double liveness = Double.parseDouble(tokens[12]);
        double speechiness = Double.parseDouble(tokens[13]);
        boolean explicit = tokens[14].equals("1");

        return new SpotifyTrack(id, artists, name, year, popularity, duration, tempo, loudness, valence,
                acousticness, danceability, energy, liveness, speechiness, explicit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpotifyTrack that = (SpotifyTrack) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

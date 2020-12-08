package bg.sofia.uni.fmi.mjt.spotify;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpotifyExplorer {

    private final List<SpotifyTrack> spotifyTracks;

    /**
     * Loads the dataset from the given {@code dataInput} stream.
     *
     * @param dataInput java.io.Reader input stream from which the dataset can be read
     */
    public SpotifyExplorer(Reader dataInput) {
        BufferedReader bufferedReader = new BufferedReader(dataInput);
        this.spotifyTracks = bufferedReader.lines().skip(1).map(SpotifyTrack::of).collect(Collectors.toList());
    }

    /**
     * @return all spotify tracks from the dataset as unmodifiable collection
     * If the dataset is empty, return an empty collection
     */
    public Collection<SpotifyTrack> getAllSpotifyTracks() {
        return spotifyTracks.stream()
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * @return all tracks from the spotify dataset classified as explicit as unmodifiable collection
     * If the dataset is empty or contains no tracks classified as explicit, return an empty collection
     */
    public Collection<SpotifyTrack> getExplicitSpotifyTracks() {
        return spotifyTracks.stream()
                .filter(SpotifyTrack::explicit)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns all tracks in the dataset, grouped by release year. If no tracks were released in a given year
     * it should not appear as key in the map.
     *
     * @return map with year as a key and the set of spotify tracks released this year as value.
     * If the dataset is empty, return an empty collection
     */
    public Map<Integer, Set<SpotifyTrack>> groupSpotifyTracksByYear() {
        return spotifyTracks.stream()
                .collect(Collectors.groupingBy(SpotifyTrack::year, Collectors.toUnmodifiableSet()));
    }

    /**
     * Returns the number of years between the oldest and the newest released tracks of an artist.
     * For example, if the oldest and newest tracks are released in 1996 and 1998 respectively,
     * return 3, if the oldest and newest release match, e.g. 2002-2002, return 1.
     * Note that tracks with multiple authors including the given artist should also be considered in the result.
     *
     * @param artist artist name
     * @return number of active years
     * If the dataset is empty or there are no tracks by the given artist in the dataset, return 0.
     */
    public int getArtistActiveYears(String artist) {
        if (artist == null) {
            throw new IllegalArgumentException("Cannot get active years of artist: Null argument is passed");
        }

        List<Integer> yearsOfArtist = spotifyTracks.stream()
                .filter(t -> t.artists().contains(artist))
                .sorted(Comparator.comparingInt(SpotifyTrack::year).reversed())
                .map(SpotifyTrack::year)
                .collect(Collectors.toUnmodifiableList());

        int rangeArtistActiveYears = 0;
        if (!yearsOfArtist.isEmpty()) {
            int maxArtistActiveYear = yearsOfArtist.get(0);
            int minArtistActiveYear = yearsOfArtist.get(yearsOfArtist.size() - 1);

            rangeArtistActiveYears = maxArtistActiveYear - minArtistActiveYear + 1;
        }
        return rangeArtistActiveYears;
    }

    /**
     * Returns the @n tracks with highest valence from the 80s.
     * Note that the 80s started in 1980 and lasted until 1989, inclusive.
     * Valence describes the musical positiveness conveyed by a track.
     * Tracks with high valence sound more positive (happy, cheerful, euphoric),
     * while tracks with low valence sound more negative (sad, depressed, angry).
     *
     * @param n number of tracks to return
     *          If @n exceeds the total number of tracks from the 80s, return all tracks available from this period.
     * @return unmodifiable list of tracks sorted by valence in descending order
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public List<SpotifyTrack> getTopNHighestValenceTracksFromThe80s(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot get top " + n + " highest valence tracks from the 80s");
        }

        return spotifyTracks.stream()
                .filter(t -> t.year() >= 1980 && t.year() <= 1989 && t.valence() > 0)
                .sorted(Comparator.comparing(SpotifyTrack::valence).reversed())
                .limit(n)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns the most popular track from the 90s.
     * Note that the 90s started in 1990 and lasted until 1999, inclusive.
     * The value is between 0 and 100, with 100 being the most popular.
     *
     * @return the most popular track of the 90s.
     * If there more than one tracks with equal highest popularity, return any of them
     * @throws NoSuchElementException if there are no tracks from the 90s in the dataset
     */
    public SpotifyTrack getMostPopularTrackFromThe90s() {
        return spotifyTracks.stream()
                .filter(t -> t.year() >= 1990 && t.year() <= 1999)
                .max(Comparator.comparing(SpotifyTrack::popularity))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Returns the number of tracks longer than @minutes released before @year.
     *
     * @param minutes
     * @param year
     * @return the number of tracks longer than @minutes released before @year
     * @throws IllegalArgumentException in case @minutes or @year is a negative number
     */
    public long getNumberOfLongerTracksBeforeYear(int minutes, int year) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Cannot get number of longer tracks: Input minutes are negative");
        }
        if (year < 0) {
            throw new IllegalArgumentException("Cannot get number of longer tracks: Input year is negative");
        }

        long milliseconds = TimeUnit.MINUTES.toMillis(minutes);
        return spotifyTracks.stream()
                .filter(t -> t.duration_ms() > milliseconds && t.year() < year)
                .count();
    }

    /**
     * Returns the loudest track released in a given year
     *
     * @param year
     * @return the loudest track released in a given year
     * @throws IllegalArgumentException in case @year is a negative number
     */
    public Optional<SpotifyTrack> getTheLoudestTrackInYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("Cannot get the loudest track in " + year + " year");
        }

        return spotifyTracks.stream()
                .filter(t -> t.year() == year)
                .max(Comparator.comparing(SpotifyTrack::loudness));
    }
}

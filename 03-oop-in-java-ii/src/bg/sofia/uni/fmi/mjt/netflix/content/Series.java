package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public final class Series extends Film {
    private final Episode[] episodes;
    public Series(String name, Genre genre, PgRating pgRating, Episode[] episodes) {
        super(name, genre, pgRating);
        this.episodes = episodes;
    }

    @Override
    public int getDuration() {
        int totalDuration = 0;
        for (Episode e: episodes) {
            totalDuration += e.duration();
        }
        return totalDuration;
    }
}

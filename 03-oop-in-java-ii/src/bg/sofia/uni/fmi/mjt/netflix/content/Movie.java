package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public final class Movie extends Film {
    private final int duration;

    public Movie(String name, Genre genre, PgRating pgRating, int duration) {
        super(name, genre, pgRating);
        this.duration = duration;
    }

    @Override
    public int getDuration() {
        return this.duration;
    }
}

package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

public sealed interface Streamable permits Film {
    String getTitle();
    int getDuration();
    PgRating getRating();
}

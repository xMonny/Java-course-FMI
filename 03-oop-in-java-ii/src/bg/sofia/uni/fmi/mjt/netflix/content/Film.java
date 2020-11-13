package bg.sofia.uni.fmi.mjt.netflix.content;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.Genre;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;

import java.util.ArrayList;
import java.util.List;

public abstract sealed class Film implements Streamable permits Movie, Series{
    private final String name;
    private final Genre genre;
    private final PgRating pgRating;
    private final List<Account> viewers = new ArrayList<>();

    public Film(String name, Genre genre, PgRating pgRating) {
        this.name = name;
        this.genre = genre;
        this.pgRating = pgRating;
    }

    public void addViewer(Account user) {
        viewers.add(user);
    }

    public List<Account> getViewers() {
        return this.viewers;
    }

    public Genre getGenre() {
        return this.genre;
    }

    @Override
    public String getTitle() {
        return this.name;
    }

    @Override
    public PgRating getRating() {
        return this.pgRating;
    }

    @Override
    public abstract int getDuration();
}

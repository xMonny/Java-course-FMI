package bg.sofia.uni.fmi.mjt.netflix.platform;

import bg.sofia.uni.fmi.mjt.netflix.account.Account;
import bg.sofia.uni.fmi.mjt.netflix.content.Film;
import bg.sofia.uni.fmi.mjt.netflix.content.Streamable;
import bg.sofia.uni.fmi.mjt.netflix.content.enums.PgRating;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.ContentUnavailableException;
import bg.sofia.uni.fmi.mjt.netflix.exceptions.UserNotFoundException;

import java.time.LocalDateTime;

public final class Netflix implements StreamingService {
    private final Account[] accounts;
    private final Streamable[] streamables;

    public Netflix(Account[] accounts, Streamable[] streamableContent) {
        this.accounts = accounts;
        this.streamables = streamableContent;
    }

    private boolean isAccountRegistered(Account user) throws UserNotFoundException{
        for(Account acc: accounts) {
            if (acc.equals(user)) {
                return true;
            }
        }
        throw new UserNotFoundException(user);
    }

    private boolean isContentInPlatform(String videoContentName) throws ContentNotFoundException {
        if (findByName(videoContentName) != null) {
            return true;
        }
        throw new ContentNotFoundException(videoContentName);
    }

    private PgRating getRatingTypeFor(String videoContentName) {
        Streamable stream = findByName(videoContentName);
        if (stream == null) {
            throw new IllegalArgumentException("Video content cannot be null");
        }
        return stream.getRating();
    }

    private int getRatingAgeRestriction(PgRating ratingType) {
        int restriction = switch(ratingType) {
                case G -> 0;
                case PG13 -> 14;
                case NC17 -> 18;
            };
        return restriction;
    }

    private String getRatingRestrictionMessage(PgRating ratingType) {
        return ratingType.getDetails();
    }

    private boolean isContentAvailableFor(Account user, String videoContentName) {
        int userBirthYear = user.birthDate().getYear();
        int currentYear = LocalDateTime.now().getYear();
        int userAge = currentYear - userBirthYear;

        PgRating ratingType = getRatingTypeFor(videoContentName);
        int ratingYearRestriction = getRatingAgeRestriction(ratingType);
        return userAge >= ratingYearRestriction;
    }

    @Override
    public void watch(Account user, String videoContentName) throws ContentUnavailableException {
        if (!isAccountRegistered(user) || !isContentInPlatform(videoContentName)) {
            return;
        }

        if (!isContentAvailableFor(user, videoContentName)) {
            PgRating ratingType = getRatingTypeFor(videoContentName);
            String message = getRatingRestrictionMessage(ratingType);
            throw new ContentUnavailableException(videoContentName, message);
        }

        Streamable stream = findByName(videoContentName);
        if (stream != null) {
            ((Film) stream).addViewer(user);
        }
        else {
            throw new IllegalArgumentException("Video content cannot be null");
        }
    }

    @Override
    public Streamable findByName(String videoContentName) {
        for (Streamable stream: streamables) {
            if (stream.getTitle().equals(videoContentName)) {
                return stream;
            }
        }
        return null;
    }

    @Override
    public Streamable mostViewed() {
        Streamable stream = null;
        int max = 0;
        for (Streamable s: streamables) {
            int numberViewers = ((Film) s).getViewers().size();
            if (numberViewers > max) {
                max = numberViewers;
                stream = s;
            }
        }
        return stream;
    }

    @Override
    public int totalWatchedTimeByUsers() {
        int totalTime = 0;
        for (Streamable s: streamables) {
            int numberViewers = ((Film) s).getViewers().size();
            int streamDuration = s.getDuration();
            totalTime += numberViewers * streamDuration;
        }
        return totalTime;
    }
}

package bg.sofia.uni.fmi.mjt.socialmedia.content;

import bg.sofia.uni.fmi.mjt.socialmedia.content.enums.ContentType;

import java.time.LocalDateTime;

public final class Story extends AbstractContent {
    private static final int EXPIRATION_24_HOURS = 24;

    public Story(String id, LocalDateTime publishedOn, String description) {
        super(id, publishedOn, description);
        this.expirationDate = publishedOn.plusHours(EXPIRATION_24_HOURS);
        this.contentType = ContentType.STORY;
    }
}

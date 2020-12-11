package bg.sofia.uni.fmi.mjt.socialmedia.content;

import bg.sofia.uni.fmi.mjt.socialmedia.content.enums.ContentType;

import java.time.LocalDateTime;

public final class Post extends AbstractContent {
    private static final int EXPIRATION_30_DAYS = 30;

    public Post(String id, LocalDateTime publishedOn, String description) {
        super(id, publishedOn, description);
        this.expirationDate = publishedOn.plusDays(EXPIRATION_30_DAYS);
        this.contentType = ContentType.POST;
    }
}

package bg.sofia.uni.fmi.mjt.socialmedia.content;

import bg.sofia.uni.fmi.mjt.socialmedia.content.enums.ContentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public abstract class AbstractContent implements Content {
    private final String id;
    private final String description;
    private final LocalDateTime publishedOn;
    protected LocalDateTime expirationDate;
    private int numberOfLikes;
    private int numberOfComments;
    protected ContentType contentType;

    public AbstractContent(String id, LocalDateTime publishedOn, String description) {
        this.id = id;
        this.publishedOn = publishedOn;
        this.description = description;
    }

    public LocalDateTime getPublishedOn() {
        return this.publishedOn;
    }

    public void increaseLikes() {
        this.numberOfLikes++;
    }

    public void decreaseLikes() {
        if (numberOfLikes > 0) {
            this.numberOfLikes--;
        }
    }

    public void increaseComments() {
        this.numberOfComments++;
    }

    public boolean isNotExpired() {
        return this.expirationDate.isAfter(LocalDateTime.now());
    }

    public String getContentNameType() {
        return this.contentType.getContentNameType();
    }

    public int getPopularity() {
        return this.numberOfLikes + this.numberOfComments;
    }

    public boolean containsTag(String tag) {
        Collection<String> tags = getTags();
        for (String s : tags) {
            if (s.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getNumberOfLikes() {
        return this.numberOfLikes;
    }

    @Override
    public int getNumberOfComments() {
        return this.numberOfComments;
    }

    @Override
    public String getId() {
        return this.id;
    }

    private Collection<String> getSpecifiedDescription(String sign) {
        String[] splitedDescription = this.description.split("\\s+");
        List<String> specifiedDescription = new ArrayList<>();
        for (String s : splitedDescription) {
            if (s.startsWith(sign)) {
                specifiedDescription.add(s);
            }
        }
        return specifiedDescription;
    }

    @Override
    public Collection<String> getTags() {
        return getSpecifiedDescription("#");
    }

    @Override
    public Collection<String> getMentions() {
        return getSpecifiedDescription("@");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractContent)) {
            return false;
        }
        AbstractContent abstractContent = (AbstractContent) o;
        return getId().equals(abstractContent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

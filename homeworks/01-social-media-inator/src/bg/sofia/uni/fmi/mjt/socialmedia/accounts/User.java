package bg.sofia.uni.fmi.mjt.socialmedia.accounts;

import bg.sofia.uni.fmi.mjt.socialmedia.content.AbstractContent;

import bg.sofia.uni.fmi.mjt.socialmedia.accounts.log.Activity;

import java.time.LocalDateTime;

import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;

public class User {
    private final String username;
    private int numberOfMentions;

    private final Map<String, AbstractContent> contents;
    private final Map<String, AbstractContent> likedContents;
    private final Set<Activity> activityLog;

    public User(String username) {
        this.username = username;
        this.contents = new HashMap<>();
        this.likedContents = new HashMap<>();
        this.activityLog = new TreeSet<>();
    }

    private boolean isContentLiked(AbstractContent content) {
        return likedContents.containsKey(content.getId());
    }

    public String getUsername() {
        return this.username;
    }

    public void increaseNumberOfMentions() {
        this.numberOfMentions++;
    }

    public int getNumberOfMentions() {
        return this.numberOfMentions;
    }

    public boolean hasContent(String id) {
        return contents.containsKey(id);
    }

    public AbstractContent getContent(String id) {
        return contents.get(id);
    }

    public List<AbstractContent> getAllContents() {
        return new ArrayList<>(contents.values());
    }

    private void addToActivityLog(LocalDateTime dateTime, String logText) {
        Activity activity = new Activity(dateTime, logText);
        activityLog.add(activity);
    }

    public void commentContent(AbstractContent contentToComment, String textToPublish) {
        contentToComment.increaseComments();
        String logText = "Commented \"" + textToPublish + "\" on a content with id " + contentToComment.getId();
        addToActivityLog(LocalDateTime.now(), logText);
    }

    public void likeContent(AbstractContent contentToLike) {
        if (!isContentLiked(contentToLike)) {
            contentToLike.increaseLikes();
            likedContents.put(contentToLike.getId(), contentToLike);
            String logText = "Liked a content with id " + contentToLike.getId();
            addToActivityLog(LocalDateTime.now(), logText);
        }
    }

    public void unlikeContent(AbstractContent contentToUnlike) {
        if (isContentLiked(contentToUnlike)) {
            contentToUnlike.decreaseLikes();
            likedContents.remove(contentToUnlike.getId());
            String logText = "Unliked a content with id " + contentToUnlike.getId();
            addToActivityLog(LocalDateTime.now(), logText);
        }
    }

    public void addContent(AbstractContent contentToAdd) {
        contents.put(contentToAdd.getId(), contentToAdd);
        String logText = "Created a " + contentToAdd.getContentNameType() + " with id " + contentToAdd.getId();
        addToActivityLog(contentToAdd.getPublishedOn(), logText);
    }

    public List<String> getActivityLog() {
        List<String> log = new ArrayList<>();
        for (Activity a : activityLog) {
            log.add(a.getActivityInformation());
        }
        return log;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}

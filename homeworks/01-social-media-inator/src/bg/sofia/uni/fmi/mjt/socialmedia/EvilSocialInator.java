package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.accounts.User;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.AbstractContent;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Post;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Story;
import bg.sofia.uni.fmi.mjt.socialmedia.content.comparators.ContentDataComparator;
import bg.sofia.uni.fmi.mjt.socialmedia.content.comparators.ContentPopularityComparator;
import bg.sofia.uni.fmi.mjt.socialmedia.content.enums.ContentType;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.NoUsersException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameNotFoundException;

import java.time.LocalDateTime;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class EvilSocialInator implements SocialMediaInator {
    private static final String LIKE_REACTION = "Like";
    private static final String COMMENT_REACTION = "Comment";

    private int numberOfContents;
    private final Map<String, User> users;
    private final Map<String, ContentUserPair> publishedContents;

    public EvilSocialInator() {
        this.users = new HashMap<>();
        this.publishedContents = new HashMap<>();
    }

    private boolean isUserRegistered(String username) {
        return users.containsKey(username);
    }

    private boolean isContentAvailable(String id) {
        ContentUserPair foundContentAndUser = publishedContents.get(id);
        if (foundContentAndUser == null) {
            return false;
        }
        AbstractContent content = foundContentAndUser.content();
        return content.isNotExpired();
    }

    private AbstractContent getContent(String id) {
        ContentUserPair foundContentAndUser = publishedContents.get(id);
        if (foundContentAndUser == null) {
            return null;
        }
        return foundContentAndUser.content();
    }

    @Override
    public void register(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Cannot register user: Null username is passed");
        }
        if (isUserRegistered(username)) {
            throw new UsernameAlreadyExistsException("Cannot register user: User \"" + username + "\" already exists");
        }
        User newUser = new User(username);
        users.put(username, newUser);
    }

    /**
     * get all mentions from input content
     * and distribute them between registered users
     */
    private void distributeMentions(AbstractContent contentToDistribute) {
        Collection<String> mentions = contentToDistribute.getMentions();
        for (String s : mentions) {
            String username = s.substring(1);
            if (isUserRegistered(username)) {
                users.get(username).increaseNumberOfMentions();
            }
        }
    }

    private String publish(ContentType contentType, String username, LocalDateTime publishedOn, String description) {
        if (username == null) {
            throw new IllegalArgumentException("Cannot publish: Null username is passed");
        }
        if (publishedOn == null) {
            throw new IllegalArgumentException("Cannot publish: Null date time is passed");
        }
        if (description == null) {
            throw new IllegalArgumentException("Cannot publish: Null description is passed");
        }
        if (!isUserRegistered(username)) {
            throw new UsernameNotFoundException("Cannot publish: Username \"" + username + "\" doesn't exist");
        }

        String contentId = username + "-" + numberOfContents;
        AbstractContent contentToAdd;
        if (contentType.equals(ContentType.POST)) {
            contentToAdd = new Post(contentId, publishedOn, description);
        } else {
            contentToAdd = new Story(contentId, publishedOn, description);
        }
        users.get(username).addContent(contentToAdd);
        publishedContents.put(contentId, new ContentUserPair(contentToAdd, users.get(username)));
        distributeMentions(contentToAdd);
        numberOfContents++;

        return contentId;
    }

    @Override
    public String publishPost(String username, LocalDateTime publishedOn, String description) {
        return publish(ContentType.POST, username, publishedOn, description);
    }

    @Override
    public String publishStory(String username, LocalDateTime publishedOn, String description) {
        return publish(ContentType.STORY, username, publishedOn, description);
    }

    private void react(String reaction, String username, String text, String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cannot react a content: Null id is passed");
        }
        if (username == null) {
            throw new IllegalArgumentException("Cannot react a content with id \"" + id + "\":Null username is passed");
        }
        if (text == null) {
            throw new IllegalArgumentException("Cannot react a content with id \"" + id + "\": Null text is passed");
        }
        if (!isUserRegistered(username)) {
            throw new UsernameNotFoundException("Cannot react a content: Username \"" + username + "\" doesn't exist");
        }
        if (!isContentAvailable(id)) {
            throw new ContentNotFoundException("Cannot react a content: Id \"" + id + "\" doesn't exist");
        }

        AbstractContent contentToReact = getContent(id);
        if (contentToReact == null) {
            return;
        }
        if (reaction.equals(LIKE_REACTION)) {
            users.get(username).likeContent(contentToReact);
        } else if (reaction.equals(COMMENT_REACTION)) {
            users.get(username).commentContent(contentToReact, text);
        }
    }

    @Override
    public void like(String username, String id) {
        react(LIKE_REACTION, username, "", id);
    }

    @Override
    public void comment(String username, String text, String id) {
        react(COMMENT_REACTION, username, text, id);
    }

    private Collection<Content> takeNecessaryContentFrom(List<Content> availableContents, int n) {
        List<Content> necessaryContents = new ArrayList<>();
        if (availableContents.size() <= n) {
            necessaryContents = availableContents;
        } else {
            for (int i = 0; i < n; i++) {
                necessaryContents.add(availableContents.get(i));
            }
        }
        return Collections.unmodifiableCollection(necessaryContents);
    }

    @Override
    public Collection<Content> getNMostPopularContent(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot get " + n + " most popular content");
        }
        List<Content> availableContents = new ArrayList<>();
        for (ContentUserPair p : publishedContents.values()) {
            AbstractContent content = p.content();
            if (content.isNotExpired()) {
                availableContents.add(content);
            }
        }
        availableContents.sort(new ContentPopularityComparator());
        return takeNecessaryContentFrom(availableContents, n);
    }

    @Override
    public Collection<Content> getNMostRecentContent(String username, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot get " + n + " most recent contents");
        }
        if (username == null) {
            throw new IllegalArgumentException("Cannot get " + n + " most recent contents: Null username is passed");
        }
        if (!isUserRegistered(username)) {
            throw new UsernameNotFoundException("In most recent contents: Username \"" + username + "\" doesn't exist");
        }

        List<AbstractContent> allUserContents = users.get(username).getAllContents();
        List<Content> availableContents = new ArrayList<>();
        for (AbstractContent e : allUserContents) {
            if (e.isNotExpired()) {
                availableContents.add(e);
            }
        }
        availableContents.sort(new ContentDataComparator());
        return takeNecessaryContentFrom(availableContents, n);
    }

    @Override
    public String getMostPopularUser() {
        if (users.isEmpty()) {
            throw new NoUsersException("Cannot get most popular user: There are no users registered");
        }
        int maxNumberOfMentions = 0;
        User mostPopularUser = null;
        for (User u : users.values()) {
            if (u.getNumberOfMentions() >= maxNumberOfMentions) {
                maxNumberOfMentions = u.getNumberOfMentions();
                mostPopularUser = u;
            }
        }
        return mostPopularUser.getUsername();
    }

    @Override
    public Collection<Content> findContentByTag(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException("Cannot find content by tag: Null tag is passed");
        }
        List<Content> contents = new ArrayList<>();
        for (User u : users.values()) {
            List<AbstractContent> userContents = u.getAllContents();
            for (AbstractContent e : userContents) {
                if (e.isNotExpired() && e.containsTag(tag)) {
                    contents.add(e);
                }
            }
        }
        return Collections.unmodifiableCollection(contents);
    }

    @Override
    public List<String> getActivityLog(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Cannot get activity log: Null username is passed");
        }
        if (!isUserRegistered(username)) {
            throw new UsernameNotFoundException("Cannot get activity log: \"" + username + "\" doesn't exist");
        }
        return users.get(username).getActivityLog();
    }
}

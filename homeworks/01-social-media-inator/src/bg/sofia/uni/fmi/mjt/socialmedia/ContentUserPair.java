package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.accounts.User;
import bg.sofia.uni.fmi.mjt.socialmedia.content.AbstractContent;

public record ContentUserPair(AbstractContent content, User user) { }

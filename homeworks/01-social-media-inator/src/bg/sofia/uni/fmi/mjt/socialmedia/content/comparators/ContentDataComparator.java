package bg.sofia.uni.fmi.mjt.socialmedia.content.comparators;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.AbstractContent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class ContentDataComparator implements Comparator<Content> {
    public int compare(Content o1, Content o2) {
        LocalDateTime date1 = ((AbstractContent) o1).getPublishedOn();
        LocalDateTime date2 = ((AbstractContent) o2).getPublishedOn();
        long difference = Duration.between(date1, date2).toMillis();
        if (difference <= 0) {
            return -1;
        }
        return 1;
    }
}

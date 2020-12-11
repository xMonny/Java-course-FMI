package bg.sofia.uni.fmi.mjt.socialmedia.content.comparators;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.AbstractContent;

import java.util.Comparator;

public class ContentPopularityComparator implements Comparator<Content> {
    public int compare(Content o1, Content o2) {
        int popularityDifference = ((AbstractContent) o1).getPopularity() - ((AbstractContent) o2).getPopularity();
        if (popularityDifference > 0) {
            return -1;
        } else if (popularityDifference < 0) {
            return 1;
        }
        return 0;
    }
}

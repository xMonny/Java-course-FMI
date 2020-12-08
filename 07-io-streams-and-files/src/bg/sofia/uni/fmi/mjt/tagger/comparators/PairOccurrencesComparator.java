package bg.sofia.uni.fmi.mjt.tagger.comparators;

import bg.sofia.uni.fmi.mjt.tagger.Pair;

import java.util.Comparator;

public class PairOccurrencesComparator implements Comparator<Pair> {
    public int compare(Pair p1, Pair p2) {
        return p2.occurrences() - p1.occurrences();
    }
}

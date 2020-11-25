package bg.sofia.uni.fmi.mjt.shopping.item.comparators;

import bg.sofia.uni.fmi.mjt.shopping.item.Pair;

import java.util.Comparator;

public class PairQuantityComparator implements Comparator<Pair> {
    public int compare(Pair p1, Pair p2) {
        if (p1.quantity() < p2.quantity()) {
            return 1;
        } else if (p1.quantity() > p2.quantity()) {
            return -1;
        }
        return 0;
    }
}

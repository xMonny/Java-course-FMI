package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import java.util.Objects;

public abstract class Potion implements Treasure {

    private final int points;

    public Potion(int points) {
        this.points = points;
    }

    public int heal() {
        return this.points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Potion potion = (Potion) o;
        return points == potion.points;
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }
}

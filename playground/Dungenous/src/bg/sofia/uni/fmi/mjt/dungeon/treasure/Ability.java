package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import java.util.Objects;

public abstract class Ability implements Treasure {

    private final String name;
    private final int damage;
    private final int cost;

    public Ability(String name, int damage, int cost) {
        this.name = name;
        this.damage = damage;
        this.cost = cost;
    }

    public String getName() {
        return this.name;
    }

    public int getDamage() {
        return this.damage;
    }

    int getCost() {
        return this.cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ability ability = (Ability) o;
        return damage == ability.damage
                && cost == ability.cost
                && name.equals(ability.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, damage, cost);
    }
}

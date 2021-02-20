package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class Weapon extends Ability {

    public Weapon(String name, int damage) {
        super(name, damage, 0);
    }

    public String collect(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Cannot collect to null hero");
        }
        hero.equip(this);
        return "Weapon found! Damage points: " + getDamage();
    }
}

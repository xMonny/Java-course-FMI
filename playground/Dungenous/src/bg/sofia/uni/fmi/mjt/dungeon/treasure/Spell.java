package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class Spell extends Ability {

    public Spell(String name, int damage, int manaCost) {
        super(name, damage, manaCost);
    }

    public int getManaCost() {
        return getCost();
    }

    public String collect(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Cannot collect to null hero");
        }
        hero.learn(this);
        return "Spell found! Damage points: " + getDamage() + ", Mana cost: " + getManaCost();
    }
}

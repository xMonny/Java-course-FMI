package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class ManaPotion extends Potion {

    public ManaPotion(int manaPoints) {
        super(manaPoints);
    }

    @Override
    public String collect(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Cannot collect to null hero");
        }
        int manaPoints = heal();
        if (hero.isAlive()) {
            hero.takeMana(manaPoints);
        }
        return "Mana potion found! " + manaPoints + " mana points added to your hero!";
    }
}

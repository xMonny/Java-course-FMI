package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class HealthPotion extends Potion {

    public HealthPotion(int healingPoints) {
        super(healingPoints);
    }

    @Override
    public String collect(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Cannot collect to null hero");
        }
        int healingPoints = heal();
        if (hero.isAlive()) {
            hero.takeHealing(healingPoints);
        }
        return "Health potion found! " + healingPoints + " health points added to your hero!";
    }
}

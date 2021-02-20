package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ManaPotionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCollectWhenHeroIsNull() {
        Treasure manaPotion = new ManaPotion(10);
        manaPotion.collect(null);
    }

    @Test
    public void testCollectWhenHeroIsDead() {
        Hero hero = new Hero("Hero", 0, 20);
        Treasure manaPotion = new ManaPotion(10);
        String expectedMessage = "Mana potion found! 10 mana points added to your hero!";
        String actualMessage = manaPotion.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(20, hero.getMana());
    }

    @Test
    public void testCollectWhenHeroHasLowMana() {
        Hero hero = new Hero("Hero", 100, 20);
        Spell fire = new Spell("FIRE", 30, 11);
        hero.learn(fire);
        hero.attack();
        Treasure manaPotion = new ManaPotion(10);
        String expectedMessage = "Mana potion found! 10 mana points added to your hero!";
        String actualMessage = manaPotion.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(19, hero.getMana());
    }

    @Test
    public void testCollectWhenHeroHasLessManaAndPotionWillGiveMoreThanStartingMana() {
        Hero hero = new Hero("Hero", 100, 20);
        Spell fire = new Spell("FIRE", 30, 1);
        hero.learn(fire);
        hero.attack();
        Treasure manaPotion = new ManaPotion(10);
        String expectedMessage = "Mana potion found! 10 mana points added to your hero!";
        String actualMessage = manaPotion.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(20, hero.getMana());
    }
}

package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HealthPotionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCollectWhenHeroIsNull() {
        Treasure healthPotion = new HealthPotion(10);
        healthPotion.collect(null);
    }

    @Test
    public void testCollectWhenHeroIsDead() {
        Hero hero = new Hero("Hero", 0, 20);
        Treasure healthPotion = new HealthPotion(10);
        String expectedMessage = "Health potion found! 10 health points added to your hero!";
        String actualMessage = healthPotion.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(0, hero.getHealth());
    }

    @Test
    public void testCollectWhenHeroHasLowHealth() {
        Hero hero = new Hero("Hero", 50, 20);
        hero.takeDamage(20);
        Treasure healthPotion = new HealthPotion(10);
        String expectedMessage = "Health potion found! 10 health points added to your hero!";
        String actualMessage = healthPotion.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(40, hero.getHealth());
    }

    @Test
    public void testCollectWhenHeroHasLessHealthAndPotionWillGiveMoreThanStartingHealth() {
        Hero hero = new Hero("Hero", 50, 20);
        hero.takeDamage(1);
        Treasure healthPotion = new HealthPotion(10);
        String expectedMessage = "Health potion found! 10 health points added to your hero!";
        String actualMessage = healthPotion.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(50, hero.getHealth());
    }
}

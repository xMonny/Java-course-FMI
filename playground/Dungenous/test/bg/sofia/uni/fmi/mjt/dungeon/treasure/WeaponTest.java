package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeaponTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCollectWhenHeroIsNull() {
        Treasure ak47 = new Weapon("AK-47", 10);
        ak47.collect(null);
    }

    @Test
    public void testCollect() {
        Hero hero = new Hero("Hero", 10, 20);
        Treasure ak47 = new Weapon("AK-47", 10);
        String expectedMessage = "Weapon found! Damage points: 10";
        String actualMessage = ak47.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(ak47, hero.getWeapon());
    }
}

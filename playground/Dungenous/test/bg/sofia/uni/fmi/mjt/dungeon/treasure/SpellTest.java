package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SpellTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCollectWhenHeroIsNull() {
        Treasure fire = new Spell("FIRE", 10, 20);
        fire.collect(null);
    }

    @Test
    public void testCollect() {
        Hero hero = new Hero("Hero", 10, 20);
        Treasure fire = new Spell("FIRE", 10, 20);
        String expectedMessage = "Spell found! Damage points: 10, Mana cost: 20";
        String actualMessage = fire.collect(hero);
        assertEquals(expectedMessage, actualMessage);
        assertEquals(fire, hero.getSpell());
    }
}

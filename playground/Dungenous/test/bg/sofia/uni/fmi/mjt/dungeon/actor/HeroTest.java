package bg.sofia.uni.fmi.mjt.dungeon.actor;

import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HeroTest {

    private Hero monnyAliveHero;
    private Hero monnyDeadHero;

    @Before
    public void prepareForTests() {
        monnyAliveHero = new Hero("Monny", 150, 51);
        monnyDeadHero = new Hero("Monny", 0, 51);
    }

    @Test
    public void testDefaultHeroMissingWeapon() {
        assertNull(monnyAliveHero.getWeapon());
    }

    @Test
    public void testDefaultHeroMissingSpell() {
        assertNull(monnyAliveHero.getSpell());
    }

    @Test
    public void testIsAliveWhenHealthIs0() {
        assertFalse(monnyDeadHero.isAlive());
    }

    @Test
    public void testIsAliveWhenHealthIsGreaterThan0() {
        assertTrue(monnyAliveHero.isAlive());
    }

    @Test
    public void testTakeHealingWhenDead() {
        monnyDeadHero.takeHealing(10);
        assertEquals(0, monnyDeadHero.getHealth());
    }

    @Test
    public void testTakeManaWhenDead() {
        monnyDeadHero.takeMana(10);
        assertEquals(51, monnyDeadHero.getMana());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquipNullWeapon() {
        monnyAliveHero.equip(null);
    }

    @Test
    public void testEquipWeaponWhenNotHavingOwnWeapon() {
        Weapon ak47 = new Weapon("AK-47", 40);
        monnyAliveHero.equip(ak47);
        assertEquals(ak47, monnyAliveHero.getWeapon());
    }

    @Test
    public void testEquipWeaponWhenHavingStrongerWeapon() {
        Weapon ak47 = new Weapon("AK-47", 40);
        monnyAliveHero.equip(ak47);
        Weapon m4a = new Weapon("M4A", 20);
        monnyAliveHero.equip(m4a);
        assertEquals(ak47, monnyAliveHero.getWeapon());
    }

    @Test
    public void testEquipWeaponWhenHavingWeakerWeapon() {
        Weapon ak47 = new Weapon("AK-47", 20);
        monnyAliveHero.equip(ak47);
        Weapon m4a = new Weapon("M4A", 40);
        monnyAliveHero.equip(m4a);
        assertEquals(m4a, monnyAliveHero.getWeapon());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLearnNullSpell() {
        monnyAliveHero.learn(null);
    }

    @Test
    public void testLearnWhenNotHavingOwnSpell() {
        Spell fire = new Spell("FIRE", 40, 0);
        monnyAliveHero.learn(fire);
        assertEquals(fire, monnyAliveHero.getSpell());
    }

    @Test
    public void testLearnSpellWhenHavingStrongerSpell() {
        Spell fire = new Spell("FIRE", 40, 0);
        monnyAliveHero.learn(fire);
        Spell water = new Spell("WATER", 20, 0);
        monnyAliveHero.learn(water);
        assertEquals(fire, monnyAliveHero.getSpell());
    }

    @Test
    public void testLearnSpellWhenHavingWeakerSpell() {
        Spell fire = new Spell("FIRE", 20, 0);
        monnyAliveHero.learn(fire);
        Spell water = new Spell("WATER", 40, 0);
        monnyAliveHero.learn(water);
        assertEquals(water, monnyAliveHero.getSpell());
    }

    @Test
    public void testTakeHealingWhenHeroIsDead() {
        monnyDeadHero.takeHealing(10);
        assertEquals(0, monnyDeadHero.getHealth());
    }

    @Test
    public void testTakeHealingWhenHeroIsAliveAndHealingPointsAreLessThanStartingHealth() {
        monnyAliveHero.takeDamage(100);
        monnyAliveHero.takeHealing(10);
        assertEquals(60, monnyAliveHero.getHealth());
    }

    @Test
    public void testTakeHealingWhenHeroIsAliveAndHealingPointsAreMoreThanStartingHealth() {
        monnyAliveHero.takeDamage(100);
        monnyAliveHero.takeHealing(200);
        assertEquals(150, monnyAliveHero.getHealth());
    }

    @Test
    public void testAttackDecreasingManaWhenHaveEnoughManaToUseSpell() {
        Spell fire = new Spell("FIRE", 100, 50);
        monnyAliveHero.learn(fire);
        monnyAliveHero.attack();
        assertEquals(1, monnyAliveHero.getMana());
    }

    @Test
    public void testAttackDecreasingManaWhenHaveNoEnoughManaToUseSpell() {
        Spell fire = new Spell("FIRE", 100, 500);
        monnyAliveHero.learn(fire);
        monnyAliveHero.attack();
        assertEquals(51, monnyAliveHero.getMana());
    }

    @Test
    public void testTakeManaWhenHeroIsDead() {
        monnyDeadHero.takeMana(10);
        assertEquals(51, monnyDeadHero.getMana());
    }

    @Test
    public void testTakeManaWhenHeroIsAliveAndManaPointsAreLessThanStartingMana() {
        Spell fire = new Spell("FIRE", 100, 50);
        monnyAliveHero.learn(fire);
        monnyAliveHero.attack();
        monnyAliveHero.takeMana(10);
        assertEquals(11, monnyAliveHero.getMana());
    }

    @Test
    public void testTakeManaWhenHeroIsAliveAndManaPointsAreMoreThanStartingMana() {
        Spell fire = new Spell("FIRE", 100, 50);
        monnyAliveHero.learn(fire);
        monnyAliveHero.attack();
        monnyAliveHero.takeMana(200);
        assertEquals(51, monnyAliveHero.getMana());
    }
}

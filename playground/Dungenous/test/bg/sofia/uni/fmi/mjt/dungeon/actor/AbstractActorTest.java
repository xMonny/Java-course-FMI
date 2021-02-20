package bg.sofia.uni.fmi.mjt.dungeon.actor;

import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractActorTest {

    private AbstractActor monnyAliveHero;
    private AbstractActor monnyDeadHero;
    private AbstractActor simonaAliveEnemy;
    private static final Weapon ak47 = new Weapon("AK-47", 50);
    private static final Spell fire = new Spell("FIRE", 30, 20);
    private static final Spell strongerCheaperFire = new Spell("FIRE", 100, 20);
    private static final Spell water = new Spell("WATER", 30, 200);
    private static final Spell strongerExpensiveWater = new Spell("WATER", 300, 200);

    @Before
    public void prepareForTests() {
        monnyAliveHero = new Hero("Monny", 150, 51);
        monnyDeadHero = new Hero("Monny", 0, 51);
        simonaAliveEnemy = new Enemy("Simona", 100, 30, ak47, fire);
    }

    @Test
    public void testGetNameSimonaAliveEnemy() {
        assertEquals("Simona", simonaAliveEnemy.getName());
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
    public void testTakeDamageWhenActorIsDead() {
        monnyDeadHero.takeDamage(100);
        assertEquals(0, monnyDeadHero.getHealth());
    }

    @Test
    public void testTakeDamageWhenActorIsAlive() {
        monnyAliveHero.takeDamage(100);
        assertEquals(50, monnyAliveHero.getHealth());
    }

    @Test
    public void testTakeGreaterDamageThanHealthWhenActorIsAlive() {
        monnyAliveHero.takeDamage(200);
        assertEquals(0, monnyAliveHero.getHealth());
        assertFalse(monnyAliveHero.isAlive());
    }

    @Test
    public void testAttackPointsWhenHaveNoWeaponAndSpell() {
        assertEquals(0, monnyAliveHero.attack());
    }

    @Test
    public void testAttackPointsWhenHaveOnlyWeapon() {
        simonaAliveEnemy = new Enemy("Simona", 100, 30, ak47, null);
        assertEquals(ak47.getDamage(), simonaAliveEnemy.attack());
    }

    @Test
    public void testAttackPointsWhenHaveOnlySpellAndEnoughManaToUse() {
        simonaAliveEnemy = new Enemy("Simona", 100, 30, null, fire);
        assertEquals(fire.getDamage(), simonaAliveEnemy.attack());
    }

    @Test
    public void testAttackPointsWhenHaveOnlySpellAndNotEnoughManaToUse() {
        simonaAliveEnemy = new Enemy("Simona", 100, 30, null, water);
        assertEquals(0, simonaAliveEnemy.attack());
    }

    @Test
    public void testAttackPointsWhenHaveWeaponAndStrongerSpellAndEnoughManaToUseSpell() {
        simonaAliveEnemy = new Enemy("Simona", 100, 30, ak47, strongerCheaperFire);
        assertEquals(strongerCheaperFire.getDamage(), simonaAliveEnemy.attack());
    }

    @Test
    public void testAttackPointsWhenHaveWeaponAndStrongerSpellAndNoEnoughManaToUseSpell() {
        simonaAliveEnemy = new Enemy("Simona", 100, 30, ak47, strongerExpensiveWater);
        assertEquals(ak47.getDamage(), simonaAliveEnemy.attack());
    }

    @Test
    public void testAttackPointsWhenHaveStrongerWeaponAndSpellAndEnoughManaToUseSpell() {
        assertEquals(ak47.getDamage(), simonaAliveEnemy.attack());
    }

    @Test
    public void testAttackPointsWhenHaveStrongerWeaponAndSpellAndNoEnoughManaToUseSpell() {
        simonaAliveEnemy = new Enemy("Simona", 100, 30, ak47, water);
        assertEquals(ak47.getDamage(), simonaAliveEnemy.attack());
    }
}

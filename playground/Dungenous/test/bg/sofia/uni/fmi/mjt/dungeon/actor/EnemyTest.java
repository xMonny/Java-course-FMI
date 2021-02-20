package bg.sofia.uni.fmi.mjt.dungeon.actor;

import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EnemyTest {

    private final static Weapon ak47 = new Weapon("AK-47", 50);
    private final static Spell fire = new Spell("FIRE", 40, 100);
    private final static Enemy monnyAliveEnemy = new Enemy("Monny", 150, 51, ak47, fire);
    private final static Enemy monnyDeadEnemy = new Enemy("Monny", 0, 51, null, null);

    @Test
    public void testDefaultEnemyHasWeapon() {
        assertEquals(ak47, monnyAliveEnemy.getWeapon());
    }

    @Test
    public void testDefaultHeroHasSpell() {
        assertEquals(fire, monnyAliveEnemy.getSpell());
    }

    @Test
    public void testDefaultEnemyCanHaveNoWeapon() {
        assertNull(monnyDeadEnemy.getWeapon());
    }

    @Test
    public void testDefaultHeroCanHaveNoSpell() {
        assertNull(monnyDeadEnemy.getSpell());
    }

    @Test
    public void testIsAliveWhenHealthIs0() {
        assertFalse(monnyDeadEnemy.isAlive());
    }

    @Test
    public void testIsAliveWhenHealthIsGreaterThan0() {
        assertTrue(monnyAliveEnemy.isAlive());
    }
}

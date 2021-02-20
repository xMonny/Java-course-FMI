package bg.sofia.uni.fmi.mjt.dungeon.actor;

import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;

import java.util.Objects;

public abstract class AbstractActor implements Actor {

    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final String name;
    private int health;
    private int mana;
    private Weapon weapon;
    private Spell spell;

    private final int startingHealth;
    private final int startingMana;

    public AbstractActor(String name, int health, int mana, Weapon weapon, Spell spell) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.weapon = weapon;
        this.spell = spell;

        this.startingHealth = health;
        this.startingMana = mana;
    }

    public AbstractActor(AbstractActor actor) {
        this.name = actor.name;
        this.health = actor.health;
        this.mana = actor.mana;
        this.weapon = actor.weapon;
        this.spell = actor.spell;

        this.startingHealth = actor.startingHealth;
        this.startingMana = actor.startingMana;
    }

    void heal(int healingPoints) {
        health += healingPoints;
        if (health > startingHealth) {
            health = startingHealth;
        }
    }

    void drinkMana(int manaPoints) {
        mana += manaPoints;
        if (mana > startingMana) {
            mana = startingMana;
        }
    }

    boolean hasWeapon() {
        return weapon != null;
    }

    void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    boolean hasSpell() {
        return spell != null;
    }

    void setSpell(Spell spell) {
        this.spell = spell;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMana() {
        return this.mana;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public Weapon getWeapon() {
        return this.weapon;
    }

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public void takeDamage(int damagePoints) {
        health = health - damagePoints;
        if (!isAlive()) {
            health = 0;
        }
    }

    private int getStrongerAbility() {
        int weaponDamage = weapon.getDamage();
        int spellDamage = spell.getDamage();
        int spellCost = spell.getManaCost();
        if (mana >= spellCost && spellDamage > weaponDamage) {
            mana -= spellCost;
            return spellDamage;
        }
        return weaponDamage;
    }

    @Override
    public int attack() {
        if (hasWeapon() && !hasSpell()) {
            return weapon.getDamage();
        } else if (!hasWeapon() && hasSpell()) {
            if (mana >= spell.getManaCost()) {
                mana -= spell.getManaCost();
                return spell.getDamage();
            }
        } else if (hasWeapon() && hasSpell()) {
            return getStrongerAbility();
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractActor that = (AbstractActor) o;
        return health == that.health
                && mana == that.mana
                && startingHealth == that.startingHealth
                && startingMana == that.startingMana
                && name.equals(that.name)
                && weapon.equals(that.weapon)
                && spell.equals(that.spell);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, health, mana, weapon, spell, startingHealth, startingMana);
    }
}

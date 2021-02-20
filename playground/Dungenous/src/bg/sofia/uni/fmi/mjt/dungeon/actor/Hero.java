package bg.sofia.uni.fmi.mjt.dungeon.actor;

import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;

public class Hero extends AbstractActor {

    public Hero(String name, int health, int mana) {
        super(name, health, mana, null, null);
    }

    public Hero(Hero copyHero) {
        super(copyHero);
    }

    public void takeHealing(int healingPoints) {
        if (isAlive()) {
            heal(healingPoints);
        }
    }

    public void takeMana(int manaPoints) {
        if (isAlive()) {
            drinkMana(manaPoints);
        }
    }

    public void equip(Weapon weapon) {
        if (weapon == null) {
            throw new IllegalArgumentException("Cannot equip null weapon");
        }
        if (!hasWeapon()) {
            setWeapon(weapon);
        } else {
            int currentWeaponDamage = getWeapon().getDamage();
            int newWeaponDamage = weapon.getDamage();
            if (currentWeaponDamage < newWeaponDamage) {
                setWeapon(weapon);
            }
        }
    }

    public void learn(Spell spell) {
        if (spell == null) {
            throw new IllegalArgumentException("Cannot learn null spell");
        }
        if (!hasSpell()) {
            setSpell(spell);
        } else {
            int currentSpellDamage = getSpell().getDamage();
            int newSpellDamage = spell.getDamage();
            if (currentSpellDamage < newSpellDamage) {
                setSpell(spell);
            }
        }
    }
}

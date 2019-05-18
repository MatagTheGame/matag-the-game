package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.modifiers.TappedModifier;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class CardModifiers {
    private TappedModifier tapped;
    private boolean summoningSickness;
    private boolean attacking;
    private List<Integer> blocking = new ArrayList<>();
    private int damage;
    private List<Ability> abilities = new ArrayList<>();
    private List<Ability> abilitiesUntilEndOfTurn = new ArrayList<>();
    private List<Object> targets = new ArrayList<>();

    public TappedModifier getTapped() {
        return tapped;
    }

    public boolean isTapped() {
        return tapped != null;
    }

    public boolean isUnapped() {
        return !isTapped();
    }

    public boolean isSummoningSickness() {
        return summoningSickness;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public List<Integer> getBlocking() {
        return blocking;
    }

    public boolean isBlocking() {
        return !blocking.isEmpty();
    }

    public void tap() {
        this.tapped = TappedModifier.TAPPED;
    }

    public void untap() {
        this.tapped = null;
    }

    public void setSummoningSickness(boolean summoningSickness) {
        this.summoningSickness = summoningSickness;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setBlocking(List<Integer> blocking) {
        this.blocking = blocking;
    }

    public void addBlocking(int blocking) {
        this.blocking.add(blocking);
    }

    public int getDamage() {
        return damage;
    }

    public void dealDamage(int damage) {
        this.damage += damage;
    }

    public void resetDamage() {
        this.damage = 0;
    }

    public void setTapped(TappedModifier tapped) {
        this.tapped = tapped;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public List<Ability> getAbilitiesUntilEndOfTurn() {
        return abilitiesUntilEndOfTurn;
    }

    public void setAbilitiesUntilEndOfTurn(List<Ability> abilitiesUntilEndOfTurn) {
        this.abilitiesUntilEndOfTurn = abilitiesUntilEndOfTurn;
    }

    public void setTargets(List<Object> targets) {
        this.targets = targets;
    }

    public List<Object> getTargets() {
        return targets;
    }
}

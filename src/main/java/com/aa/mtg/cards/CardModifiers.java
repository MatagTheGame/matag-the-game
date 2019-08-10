package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.modifiers.PowerToughness;
import com.aa.mtg.cards.modifiers.TappedModifier;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class CardModifiers {
    private TappedModifier tapped;
    private boolean doesNotUntapNextTurn;
    private boolean summoningSickness;
    private boolean attacking;
    private Integer blockingCardId;
    private int damage;
    private List<Ability> abilities = new ArrayList<>();
    private List<Ability> abilitiesUntilEndOfTurn = new ArrayList<>();
    private PowerToughness extraPowerToughnessUntilEndOfTurn = new PowerToughness(0, 0);
    private List<Object> targets = new ArrayList<>();
    private int attachedToId;

    public TappedModifier getTapped() {
        return tapped;
    }

    public boolean isTapped() {
        return tapped != null;
    }

    public boolean isDoesNotUntapNextTurn() {
        return doesNotUntapNextTurn;
    }

    @JsonIgnore
    public boolean isUntapped() {
        return !isTapped();
    }

    public boolean isSummoningSickness() {
        return summoningSickness;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public Integer getBlockingCardId() {
        return blockingCardId;
    }

    public boolean isBlocking() {
        return blockingCardId != null;
    }

    public void tap() {
        this.tapped = TappedModifier.TAPPED;
    }

    public void untap() {
        this.tapped = null;
    }

    public void doesNotUntapNextTurn() {
        this.doesNotUntapNextTurn = true;
    }

    public void doesNotUntapNextTurn(boolean doesNotUntapNextTurn) {
        this.doesNotUntapNextTurn = doesNotUntapNextTurn;
    }

    public void setSummoningSickness(boolean summoningSickness) {
        this.summoningSickness = summoningSickness;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setBlockingCardId(Integer blockingCardId) {
        this.blockingCardId = blockingCardId;
    }

    public void unsetBlockingCardId() {
        this.blockingCardId = null;
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

    public PowerToughness getExtraPowerToughnessUntilEndOfTurn() {
        return extraPowerToughnessUntilEndOfTurn;
    }

    public void addExtraPowerToughnessUntilEndOfTurn(PowerToughness extraPowerToughnessUntilEndOfTurn) {
        int newPower = this.extraPowerToughnessUntilEndOfTurn.getPower() + extraPowerToughnessUntilEndOfTurn.getPower();
        int newToughness = this.extraPowerToughnessUntilEndOfTurn.getToughness() + extraPowerToughnessUntilEndOfTurn.getToughness();
        this.extraPowerToughnessUntilEndOfTurn = new PowerToughness(newPower, newToughness);
    }

    public void resetExtraPowerToughnessUntilEndOfTurn() {
        extraPowerToughnessUntilEndOfTurn = new PowerToughness(0, 0);
    }

    public void setTargets(List<Object> targets) {
        this.targets = targets;
    }

    public List<Object> getTargets() {
        return targets;
    }

    public void setAttachedToId(int attachedToId) {
        this.attachedToId = attachedToId;
    }

    public int getAttachedToId() {
        return attachedToId;
    }

    public void unsetAttachedId() {
        attachedToId = 0;
    }
}

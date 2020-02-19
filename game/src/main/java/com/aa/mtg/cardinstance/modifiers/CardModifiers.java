package com.aa.mtg.cardinstance.modifiers;

import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cards.properties.PowerToughness;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE
)
public class CardModifiers {
    @JsonProperty
    private int permanentId;
    @JsonProperty
    private TappedModifier tapped;
    @JsonProperty
    private boolean doesNotUntapNextTurn;
    private boolean summoningSickness;
    @JsonProperty
    private boolean attacking;
    @JsonProperty
    private Integer blockingCardId;
    @JsonProperty
    private int damage;
    @JsonProperty
    private List<CardInstanceAbility> abilities = new ArrayList<>();
    @JsonProperty
    private List<CardInstanceAbility> abilitiesUntilEndOfTurn = new ArrayList<>();
    @JsonProperty
    private PowerToughness extraPowerToughnessUntilEndOfTurn = new PowerToughness(0, 0);
    @JsonProperty
    private List<Object> targets = new ArrayList<>();
    @JsonProperty
    private int attachedToId;
    private String controller;
    private String controllerUntilEndOfTurn;
    @JsonProperty
    private Counters counters = new Counters();

    public TappedModifier getTapped() {
        return tapped;
    }

    public boolean isTapped() {
        return tapped != null;
    }

    public boolean isDoesNotUntapNextTurn() {
        return doesNotUntapNextTurn;
    }

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

    public void setPermanentId(int permanentId) {
        this.permanentId = permanentId;
    }

    public int getPermanentId() {
        return permanentId;
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

    public void setTapped(TappedModifier tapped) {
        this.tapped = tapped;
    }

    public List<CardInstanceAbility> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<CardInstanceAbility> abilities) {
        this.abilities = abilities;
    }

    public List<CardInstanceAbility> getAbilitiesUntilEndOfTurn() {
        return abilitiesUntilEndOfTurn;
    }

    public void setAbilitiesUntilEndOfTurn(List<CardInstanceAbility> abilitiesUntilEndOfTurn) {
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

    public void cleanupUntilEndOfTurnModifiers() {
        resetDamage();
        resetExtraPowerToughnessUntilEndOfTurn();
        resetControllerUntilEndOfTurn();
    }

    public void resetDamage() {
        this.damage = 0;
    }

    private void resetExtraPowerToughnessUntilEndOfTurn() {
        extraPowerToughnessUntilEndOfTurn = new PowerToughness(0, 0);
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getController() {
        return controller;
    }

    public void setControllerUntilEndOfTurn(String controllerUntilEndOfTurn) {
        this.controllerUntilEndOfTurn = controllerUntilEndOfTurn;
    }

    public String getControllerUntilEndOfTurn() {
        return controllerUntilEndOfTurn;
    }

    public void resetControllerUntilEndOfTurn() {
        controllerUntilEndOfTurn = null;
    }

    public Counters getCounters() {
        return counters;
    }

    public PowerToughness getExtraPowerToughnessFromCounters() {
        return new PowerToughness(counters.getPlus1Counters(), counters.getPlus1Counters());
    }
}


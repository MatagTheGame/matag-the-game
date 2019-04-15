package com.aa.mtg.cards.ability;

import com.fasterxml.jackson.annotation.JsonValue;

import static java.util.Arrays.asList;

public enum Ability {
    DEATHTOUCH,
    FLYING,
    HASTE,
    REACH,
    VIGILANCE,
    DESTROY_TARGET_CREATURE(new DestroyTargetCreatureAction(asList("greaterOrEquals", "4")));

    private final AbilityAction abilityAction;

    Ability() {
        this(null);
    }

    Ability(AbilityAction abilityAction) {
        this.abilityAction = abilityAction;
    }

    public AbilityAction getAbilityAction() {
        return abilityAction;
    }

    @JsonValue
    public String toValue() {
        if (abilityAction == null) {
            return name();
        } else {
            return name() + ";" + abilityAction.getParameters();
        }
    }
}
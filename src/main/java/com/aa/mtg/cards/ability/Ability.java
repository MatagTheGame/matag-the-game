package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.type.AbilityType;

public class Ability {
    private final AbilityType abilityType;
    private final AbilityAction abilityAction;
    private final Target target;

    public Ability(AbilityType abilityType) {
        this(abilityType, null, null);
    }

    public Ability(AbilityType abilityType, AbilityAction abilityAction, Target target) {
        this.abilityType = abilityType;
        this.abilityAction = abilityAction;
        this.target = target;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public AbilityAction getAbilityAction() {
        return abilityAction;
    }

    public Target getTarget() {
        return target;
    }
}

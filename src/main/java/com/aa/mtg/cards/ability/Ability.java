package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.type.AbilityType;

import java.util.List;

public class Ability {
    private final AbilityType abilityType;
    private final AbilityAction abilityAction;
    private final List<Target> targets;

    public Ability(AbilityType abilityType) {
        this(abilityType, null, null);
    }

    public Ability(AbilityType abilityType, AbilityAction abilityAction, List<Target> targets) {
        this.abilityType = abilityType;
        this.abilityAction = abilityAction;
        this.targets = targets;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public AbilityAction getAbilityAction() {
        return abilityAction;
    }

    public List<Target> getTargets() {
        return targets;
    }
}

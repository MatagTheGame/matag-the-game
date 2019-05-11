package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.type.AbilityType;

import java.util.List;

import static java.util.Collections.emptyList;

public class Ability {
    private final AbilityType abilityType;
    private final List<Target> targets;
    private final List<String> parameters;

    public Ability(AbilityType abilityType) {
        this(abilityType, emptyList(), emptyList());
    }

    public Ability(AbilityType abilityType, List<Target> targets, List<String> parameters) {
        this.abilityType = abilityType;
        this.targets = targets;
        this.parameters = parameters;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public List<String> getParameters() {
        return parameters;
    }
}

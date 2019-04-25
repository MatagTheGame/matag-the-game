package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.type.AbilityType;

import java.util.List;

import static java.util.Collections.emptyList;

public class Ability {
    private final AbilityType abilityType;
    private final List<Target> targets;

    public Ability(AbilityType abilityType) {
        this(abilityType, emptyList());
    }

    public Ability(AbilityType abilityType, List<Target> targets) {
        this.abilityType = abilityType;
        this.targets = targets;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public List<Target> getTargets() {
        return targets;
    }
}

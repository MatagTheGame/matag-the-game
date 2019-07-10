package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.trigger.Trigger;
import com.aa.mtg.cards.ability.trigger.TriggerSubtype;
import com.aa.mtg.cards.ability.type.AbilityType;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class Ability {
    private final List<AbilityType> abilityTypes;
    private final List<Target> targets;
    private final List<String> parameters;
    private final Trigger trigger;

    public Ability(AbilityType abilityType) {
        this(singletonList(abilityType));
    }

    public Ability(List<AbilityType> abilityTypes) {
        this(abilityTypes, emptyList(), emptyList(), null);
    }

    public Ability(AbilityType abilityType, List<Target> targets, List<String> parameters, Trigger trigger) {
        this(singletonList(abilityType), targets, parameters, trigger);
    }

    public Ability(List<AbilityType> abilityTypes, List<Target> targets, List<String> parameters, Trigger trigger) {
        this.abilityTypes = abilityTypes;
        this.targets = targets;
        this.parameters = parameters;
        this.trigger = trigger;
    }

    public AbilityType getFirstAbilityType() {
        return abilityTypes.get(0);
    }

    public String getFirstAbilityTypeText() {
        return getFirstAbilityType().getText(parameters);
    }

    public List<AbilityType> getAbilityTypes() {
        return abilityTypes;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public boolean requiresTarget() {
        return !targets.isEmpty();
    }

    public boolean hasTriggerOfSubtype(TriggerSubtype triggerSubtype) {
        if (trigger != null) {
            return triggerSubtype.equals(trigger.getSubtype());
        }
        return false;
    }
}

package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.trigger.Trigger;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Ability {
    private final AbilityType abilityType;
    private final List<Target> targets;
    private final CardInstanceSelector cardInstanceSelector;
    private final List<String> parameters;
    private final Trigger trigger;
    private final Ability ability;

    public Ability(@JsonProperty("abilityType") AbilityType abilityType, @JsonProperty("targets") List<Target> targets,
        @JsonProperty("cardInstanceSelector") CardInstanceSelector cardInstanceSelector, @JsonProperty("parameters") List<String> parameters,
        @JsonProperty("trigger") Trigger trigger, @JsonProperty("ability") Ability ability) {
        this.abilityType = abilityType;
        this.targets = targets;
        this.cardInstanceSelector = cardInstanceSelector;
        this.parameters = parameters;
        this.trigger = trigger;
        this.ability = ability;
    }
}

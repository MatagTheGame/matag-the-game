package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.trigger.Trigger;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
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
  protected final AbilityType abilityType;
  protected final List<Target> targets;
  protected final CardInstanceSelector cardInstanceSelector;
  protected final List<String> parameters;
  protected final Trigger trigger;
  protected final Ability ability;

  public Ability(@JsonProperty("abilityType") AbilityType abilityType, @JsonProperty("targets") List<Target> targets,
                 @JsonProperty("cardInstanceSelector") CardInstanceSelector cardInstanceSelector, @JsonProperty("parameters") List<String> parameters,
                 @JsonProperty("trigger") Trigger trigger, @JsonProperty("ability") Ability ability) {
    this.abilityType = abilityType;
    this.targets = targets != null ? targets : new ArrayList<>();
    this.cardInstanceSelector = cardInstanceSelector;
    this.parameters = parameters != null ? parameters : new ArrayList<>();
    this.trigger = trigger;
    this.ability = ability;
  }
}

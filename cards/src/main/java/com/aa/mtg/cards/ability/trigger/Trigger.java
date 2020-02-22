package com.aa.mtg.cards.ability.trigger;

import static com.aa.mtg.cards.ability.trigger.TriggerType.ACTIVATED_ABILITY;
import static com.aa.mtg.cards.ability.trigger.TriggerType.CAST;
import static com.aa.mtg.cards.ability.trigger.TriggerType.MANA_ABILITY;
import static com.aa.mtg.cards.ability.trigger.TriggerType.STATIC;
import static com.aa.mtg.cards.ability.trigger.TriggerType.TRIGGERED_ABILITY;

import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
import com.aa.mtg.cards.properties.Cost;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Trigger {
  private final TriggerType type;
  private final TriggerSubtype subtype;
  private final List<Cost> cost;
  private final CardInstanceSelector cardInstanceSelector;

  @JsonCreator
  private Trigger(@JsonProperty("type") TriggerType type, @JsonProperty("subtype") TriggerSubtype subtype,
                  @JsonProperty("cost") List<Cost> cost, @JsonProperty("cardInstanceSelector") CardInstanceSelector cardInstanceSelector) {
    this.type = type;
    this.subtype = subtype;
    this.cost = cost;
    this.cardInstanceSelector = cardInstanceSelector;
  }

  public static Trigger castTrigger() {
    return new Trigger(CAST, null, null, null);
  }

  public static Trigger manaAbilityTrigger() {
    return new Trigger(MANA_ABILITY, null, null, null);
  }

  public static Trigger triggeredAbility(TriggerSubtype triggerSubtype, CardInstanceSelector cardInstanceSelector) {
    return new Trigger(TRIGGERED_ABILITY, triggerSubtype, null, cardInstanceSelector);
  }

  public static Trigger activatedAbility(List<Cost> cost) {
    return new Trigger(ACTIVATED_ABILITY, null, cost, null);
  }

  public static Trigger staticAbility() {
    return new Trigger(STATIC, null, null, null);
  }
}

package com.matag.cards.ability.trigger;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.properties.Cost;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@JsonDeserialize(builder = Trigger.TriggerBuilder.class)
@Builder(toBuilder = true)
public class Trigger {
  private final TriggerType type;
  private final TriggerSubtype subtype;
  private final List<Cost> cost;
  private final CardInstanceSelector cardInstanceSelector;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TriggerBuilder {

  }

  public static Trigger castTrigger() {
    return new Trigger(TriggerType.CAST, null, null, null);
  }

  public static Trigger manaAbilityTrigger() {
    return new Trigger(TriggerType.MANA_ABILITY, null, null, null);
  }

  public static Trigger triggeredAbility(TriggerSubtype triggerSubtype, CardInstanceSelector cardInstanceSelector) {
    return new Trigger(TriggerType.TRIGGERED_ABILITY, triggerSubtype, null, cardInstanceSelector);
  }

  public static Trigger activatedAbility(List<Cost> cost) {
    return new Trigger(TriggerType.ACTIVATED_ABILITY, null, cost, null);
  }

  public static Trigger staticAbility() {
    return new Trigger(TriggerType.STATIC, null, null, null);
  }
}

package com.matag.cards.ability.selector;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = PowerToughnessConstraint.PowerToughnessConstraintBuilder.class)
@Builder(toBuilder = true)
public class PowerToughnessConstraint {
  public enum PowerOrToughness {
    POWER, TOUGHNESS
  }

  private final PowerOrToughness powerOrToughness;
  private final PowerToughnessConstraintType powerToughnessConstraintType;
  private final int value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PowerToughnessConstraintBuilder {

  }
}

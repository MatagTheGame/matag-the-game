package com.matag.cards.ability.selector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class PowerToughnessConstraint {
  public enum PowerOrToughness {
    POWER, TOUGHNESS
  }

  private final PowerOrToughness powerOrToughness;
  private final PowerToughnessConstraintType powerToughnessConstraintType;
  private final int value;

  @JsonCreator
  public PowerToughnessConstraint(@JsonProperty("powerOrToughness") PowerOrToughness powerOrToughness,
                                  @JsonProperty("powerToughnessConstraintType") PowerToughnessConstraintType powerToughnessConstraintType, @JsonProperty("value") int value) {
    this.powerOrToughness = powerOrToughness;
    this.powerToughnessConstraintType = powerToughnessConstraintType;
    this.value = value;
  }
}

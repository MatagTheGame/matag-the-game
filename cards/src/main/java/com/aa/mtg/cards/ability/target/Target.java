package com.aa.mtg.cards.ability.target;

import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
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
public class Target {
  private final CardInstanceSelector cardInstanceSelector;
  private final boolean optional;
  private final boolean other;

  @JsonCreator
  public Target(@JsonProperty("cardInstanceSelector") CardInstanceSelector cardInstanceSelector, @JsonProperty("optional") boolean optional,
                @JsonProperty("other") boolean other) {
    this.cardInstanceSelector = cardInstanceSelector;
    this.optional = optional;
    this.other = other;
  }
}

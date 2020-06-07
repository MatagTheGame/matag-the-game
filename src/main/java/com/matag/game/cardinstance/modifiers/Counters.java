package com.matag.game.cardinstance.modifiers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.cards.ability.type.AbilityType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@EqualsAndHashCode
@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.NONE,
  setterVisibility = JsonAutoDetect.Visibility.NONE,
  getterVisibility = JsonAutoDetect.Visibility.NONE,
  isGetterVisibility = JsonAutoDetect.Visibility.NONE,
  creatorVisibility = JsonAutoDetect.Visibility.NONE
)
@Getter
public class Counters {
  @JsonProperty
  private int plus1Counters;
  private List<AbilityType> keywordCounters;

  public void addPlus1Counters(int n) {
    plus1Counters += n;
  }

  public void addKeywordCounter(AbilityType abilityType) {
    keywordCounters.add(abilityType);
  }
}

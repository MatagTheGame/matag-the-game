package com.matag.game.cardinstance.modifiers;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.cards.ability.type.AbilityType;
import com.matag.game.cardinstance.ability.CardInstanceAbility;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
  @JsonProperty
  private int minus1Counters;
  @JsonProperty
  private List<AbilityType> keywordCounters = new ArrayList<>();

  public void addPlus1Counters(int n) {
    plus1Counters += n;
    balanceCounters();
  }

  public void addMinus1Counters(int n) {
    minus1Counters += n;
    balanceCounters();
  }

  public void addKeywordCounter(AbilityType abilityType) {
    keywordCounters.add(abilityType);
  }

  public List<CardInstanceAbility> getKeywordCountersAbilities() {
    return keywordCounters.stream()
      .map(CardInstanceAbility::new)
      .map(CardInstanceAbility::getCardInstanceAbility)
      .collect(toList());
  }

  private void balanceCounters() {
    var min = Math.min(plus1Counters, minus1Counters);
    plus1Counters -= min;
    minus1Counters -= min;
  }
}

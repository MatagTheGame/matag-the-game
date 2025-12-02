package com.matag.game.cardinstance.ability;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.cards.Card;
import com.matag.cards.ability.Ability;
import com.matag.cards.ability.AbilityService;
import com.matag.cards.ability.AbilityTransposer;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.type.AbilityType;

public class CardInstanceAbility() {
  private final Ability ability;

  public CardInstanceAbility(CardInstanceAbility cardInstanceAbility) {
      ability = new Ability(
            cardInstanceAbility.ability.getAbilityType(),
            cardInstanceAbility.ability.getTargets(),
            cardInstanceAbility.ability.getMagicInstanceSelector(),
            cardInstanceAbility.ability.getParameters(),
            cardInstanceAbility.ability.getTrigger(),
            cardInstanceAbility.ability.getAbility(),
            cardInstanceAbility.ability.getSorcerySpeed(),
            cardInstanceAbility.ability.getOptional()
    );
  }

  public CardInstanceAbility(AbilityType abilityType) {
      ability = new Ability((abilityType, emptyList(), null, emptyList(), null, null, false, false);
  }

  @JsonProperty
  public String getAbilityTypeText() {
    var parametersString = new AbilityService().parametersAsString(ability.getParameters());

    if (ability.getAbilityType() == AbilityType.SELECTED_PERMANENTS_GET) {
      if (ability.getMagicInstanceSelector().getSelectorType() == SelectorType.PLAYER) {
        return String.format(ability.getAbilityType().getText(), ability.getMagicInstanceSelector().text(), parametersString) + ".";
      } else {
        return String.format(ability.getAbilityType().getText(), ability.getMagicInstanceSelector().text(), parametersString) + " until end of turn.";
      }

    } else {
      return String.format(ability.getAbilityType().getText(), parametersString);
    }
  }

  public CardInstanceAbility getAbility() {
    return getCardInstanceAbility(ability);
  }

  public String getParameter(int i) {
    if (ability.getParameters().size() > i) {
      return ability.getParameters().get(i);
    }
    return null;
  }

  public boolean requiresTarget() {
    return !ability.getTargets().isEmpty();
  }

  public static List<CardInstanceAbility> getCardInstanceAbilities(Card card) {
    return Optional.ofNullable(card)
      .map(Card::getAbilities)
      .orElse(emptyList())
      .stream()
      .map(CardInstanceAbility::getCardInstanceAbility)
      .collect(toList());
  }

  public static CardInstanceAbility getCardInstanceAbility(Ability ability) {
    return Optional.ofNullable(ability)
      .map(AbilityTransposer::transpose)
      .map(CardInstanceAbility::new)
      .orElse(null);
  }
}

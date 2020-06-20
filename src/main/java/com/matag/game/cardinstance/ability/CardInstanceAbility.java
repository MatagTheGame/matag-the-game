package com.matag.game.cardinstance.ability;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.cards.Card;
import com.matag.cards.ability.Ability;
import com.matag.cards.ability.AbilityService;
import com.matag.cards.ability.AbilityTransposer;
import com.matag.cards.ability.selector.MagicInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.target.Target;
import com.matag.cards.ability.trigger.Trigger;
import com.matag.cards.ability.type.AbilityType;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CardInstanceAbility extends Ability {
  public CardInstanceAbility(Ability ability) {
    super(ability.getAbilityType(), ability.getTargets(), ability.getMagicInstanceSelector(), ability.getParameters(),
      ability.getTrigger(), ability.getAbility(), ability.isSorcerySpeed());
  }

  public CardInstanceAbility(AbilityType abilityType) {
    super(abilityType, emptyList(), null, emptyList(), null, null, false);
  }

  public CardInstanceAbility(AbilityType abilityType, List<Target> targets, List<String> parameters, Trigger trigger) {
    super(abilityType, targets, null, parameters, trigger, null, false);
  }

  public CardInstanceAbility(AbilityType abilityType, MagicInstanceSelector magicInstanceSelector, List<String> parameters, Trigger trigger) {
    super(abilityType, emptyList(), magicInstanceSelector, parameters, trigger, null, false);
  }

  @JsonProperty
  public String getAbilityTypeText() {
    String parametersString = new AbilityService().parametersAsString(parameters);

    if (abilityType == AbilityType.SELECTED_PERMANENTS_GET) {
      if (magicInstanceSelector.getSelectorType() == SelectorType.PLAYER) {
        return String.format(abilityType.getText(), magicInstanceSelector.getText(), parametersString) + ".";
      } else {
        return String.format(abilityType.getText(), magicInstanceSelector.getText(), parametersString) + " until end of turn.";
      }

    } else {
      return String.format(abilityType.getText(), parametersString);
    }
  }

  public CardInstanceAbility getAbility() {
    return getCardInstanceAbility(ability);
  }

  public String getParameter(int i) {
    if (parameters.size() > i) {
      return parameters.get(i);
    }
    return null;
  }

  public boolean requiresTarget() {
    return !targets.isEmpty();
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

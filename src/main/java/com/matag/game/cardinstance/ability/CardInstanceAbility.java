package com.matag.game.cardinstance.ability;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.cards.Card;
import com.matag.cards.ability.Ability;
import com.matag.cards.ability.AbilityService;
import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.ability.target.Target;
import com.matag.cards.ability.trigger.Trigger;
import com.matag.cards.ability.type.AbilityType;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CardInstanceAbility extends Ability {
  public CardInstanceAbility(Ability ability) {
    super(ability.getAbilityType(), ability.getTargets(), ability.getCardInstanceSelector(), ability.getParameters(),
      ability.getTrigger(), ability.getAbility(), ability.isSorcerySpeed());
  }

  public CardInstanceAbility(AbilityType abilityType) {
    super(abilityType, emptyList(), null, emptyList(), null, null, false);
  }

  public CardInstanceAbility(AbilityType abilityType, List<Target> targets, List<String> parameters, Trigger trigger) {
    super(abilityType, targets, null, parameters, trigger, null, false);
  }

  public CardInstanceAbility(AbilityType abilityType, CardInstanceSelector cardInstanceSelector, List<String> parameters, Trigger trigger) {
    super(abilityType, emptyList(), cardInstanceSelector, parameters, trigger, null, false);
  }

  @JsonProperty
  public String getAbilityTypeText() {
    String parametersString = AbilityService.parametersAsString(parameters);

    boolean negative = parametersString.startsWith("-");
    switch (abilityType) {
      case ADD_X_LIFE:
        String verb = (negative ? "lose" : "gain") + (cardInstanceSelector.isItself() ? "" : "s");
        return String.format(abilityType.getText(), cardInstanceSelector.getText(), verb, parametersString.replace("-", ""));

      case SELECTED_PERMANENTS_GET:
        return String.format(abilityType.getText(), cardInstanceSelector.getText(), parametersString) + " until end of turn.";

      default:
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
    if (card.getAbilities() == null) {
      return null;
    }
    return card.getAbilities().stream().map(CardInstanceAbility::getCardInstanceAbility).collect(toList());
  }

  public static CardInstanceAbility getCardInstanceAbility(Ability ability) {
    return Optional.ofNullable(ability).map(CardInstanceAbility::new).orElse(null);
  }
}

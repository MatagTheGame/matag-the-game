package com.matag.game.cardinstance.ability;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import com.matag.cards.ability.Ability;
import org.springframework.stereotype.Component;

import com.matag.cards.ability.type.AbilityType;

@Component
public class CardInstanceAbilityFactory {
  private CardInstanceAbility get(String abilityType) {
    return new CardInstanceAbility(new Ability(AbilityType.valueOf(abilityType)));
  }

  public List<CardInstanceAbility> abilitiesFromParameters(List<String> parameters) {
    return parameters.stream()
      .filter(parameter -> !parameter.contains("/") && !parameter.contains(":"))
      .map(this::get)
      .collect(toList());
  }

  public Optional<CardInstanceAbility> abilityFromParameter(String parameter) {
    var abilities = abilitiesFromParameters(singletonList(parameter));
    if (abilities.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(abilities.get(0));
    }
  }
}

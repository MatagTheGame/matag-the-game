package com.matag.cards.ability;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.matag.cards.ability.type.AbilityType;

@Component
public class CardInstanceAbilityFactory {
  private Ability get(String abilityType) {
      return new Ability(new Ability(AbilityType.valueOf(abilityType)));
  }

  public List<Ability> abilitiesFromParameters(List<String> parameters) {
    return parameters.stream()
      .filter(parameter -> !parameter.contains("/") && !parameter.contains(":"))
      .map(this::get)
      .collect(toList());
  }

  public Optional<Ability> abilityFromParameter(String parameter) {
    var abilities = abilitiesFromParameters(singletonList(parameter));
    if (abilities.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(abilities.get(0));
    }
  }
}

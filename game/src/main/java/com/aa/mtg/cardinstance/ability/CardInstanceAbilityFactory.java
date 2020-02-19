package com.aa.mtg.cardinstance.ability;

import com.aa.mtg.cards.ability.Abilities;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Component
public class CardInstanceAbilityFactory {
    private CardInstanceAbility get(String ability) {
        try {
            return (CardInstanceAbility) Abilities.class.getField(ability).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Ability " + ability  + " does not exist.");
        }
    }

    public List<CardInstanceAbility> abilitiesFromParameters(List<String> parameters) {
        return parameters.stream()
                .filter(parameter -> !parameter.contains("/") && !parameter.contains(":"))
                .map(this::get)
                .collect(toList());
    }

    public Optional<CardInstanceAbility> abilityFromParameter(String parameter) {
        List<CardInstanceAbility> abilities = abilitiesFromParameters(singletonList(parameter));
        if (abilities.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(abilities.get(0));
        }
    }
}

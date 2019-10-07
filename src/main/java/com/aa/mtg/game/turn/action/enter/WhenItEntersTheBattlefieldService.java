package com.aa.mtg.game.turn.action.enter;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.cards.ability.trigger.TriggerSubtype.WHEN_IT_ENTERS_THE_BATTLEFIELD;
import static java.util.stream.Collectors.toList;

@Component
public class WhenItEntersTheBattlefieldService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhenItEntersTheBattlefieldService.class);

    void whenItEntersTheBattlefield(GameStatus gameStatus, CardInstance cardInstance) {
        List<Ability> entersTheBattlefieldAbilities = cardInstance.getAbilities().stream()
            .filter(ability -> ability.hasTriggerOfSubtype(WHEN_IT_ENTERS_THE_BATTLEFIELD))
            .collect(toList());

        if (!entersTheBattlefieldAbilities.isEmpty()) {
            cardInstance.getTriggeredAbilities().addAll(entersTheBattlefieldAbilities);
            List<AbilityType> abilityTypes = entersTheBattlefieldAbilities.stream().map(Ability::getAbilityType).collect(toList());
            LOGGER.info("Event {} triggered with abilities {} for {}.", WHEN_IT_ENTERS_THE_BATTLEFIELD, abilityTypes, cardInstance.getModifiers());
            gameStatus.getStack().add(cardInstance);
        }
    }

}

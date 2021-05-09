package com.matag.game.turn.action.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WhenTriggerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WhenTriggerService.class);

  private final MagicInstancePermanentSelectorService magicInstancePermanentSelectorService;

  public void whenTriggered(GameStatus gameStatus, CardInstance cardInstance, TriggerSubtype triggerSubtype) {
    var cardsWithTriggerAbility = gameStatus.getAllBattlefieldCardsSearch().withTriggerSubtype(triggerSubtype).getCards();

    for (var cardWithTriggerAbility : cardsWithTriggerAbility) {
      for (var ability : cardWithTriggerAbility.getAbilitiesByTriggerSubType(triggerSubtype)) {
        var selectionForCardWithTriggeredAbility = magicInstancePermanentSelectorService.select(gameStatus, cardWithTriggerAbility, ability.getTrigger().getMagicInstanceSelector());
        if (selectionForCardWithTriggeredAbility.withId(cardInstance.getId()).isPresent()) {
          cardWithTriggerAbility.getTriggeredAbilities().add(ability);
        }
      }

      if (!cardWithTriggerAbility.getTriggeredAbilities().isEmpty()) {
        LOGGER.info("{} triggered {} because of {}.", cardInstance.getIdAndName(), triggerSubtype, cardInstance.getIdAndName());
        gameStatus.getStack().push(cardWithTriggerAbility);
      }
    }
  }
}

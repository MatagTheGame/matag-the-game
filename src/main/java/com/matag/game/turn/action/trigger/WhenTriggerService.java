package com.matag.game.turn.action.trigger;

import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.MagicInstanceSelectorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class WhenTriggerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WhenTriggerService.class);

  private final MagicInstanceSelectorService magicInstanceSelectorService;

  public void whenTriggered(GameStatus gameStatus, CardInstance cardInstance, TriggerSubtype triggerSubtype) {
    List<CardInstance> cardsWithTriggerAbility = gameStatus.getAllBattlefieldCards().withTriggerSubtype(triggerSubtype).getCards();

    for (CardInstance cardWithTriggerAbility : cardsWithTriggerAbility) {
      for (CardInstanceAbility ability : cardWithTriggerAbility.getAbilitiesByTriggerSubType(triggerSubtype)) {
        CardInstanceSearch selectionForCardWithTriggeredAbility = magicInstanceSelectorService.select(gameStatus, cardWithTriggerAbility, ability.getTrigger().getMagicInstanceSelector());
        if (selectionForCardWithTriggeredAbility.withId(cardInstance.getId()).isPresent()) {
          cardWithTriggerAbility.getTriggeredAbilities().add(ability);
        }
      }

      if (!cardWithTriggerAbility.getTriggeredAbilities().isEmpty()) {
        LOGGER.info("{} triggered {} because of {}.", cardInstance.getIdAndName(), triggerSubtype, cardInstance.getIdAndName());
        gameStatus.getStack().add(cardWithTriggerAbility);
      }
    }
  }
}

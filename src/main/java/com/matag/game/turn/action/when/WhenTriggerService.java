package com.matag.game.turn.action.when;

import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class WhenTriggerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WhenTriggerService.class);

  private final CardInstanceSelectorService cardInstanceSelectorService;

  protected WhenTriggerService(CardInstanceSelectorService cardInstanceSelectorService) {
    this.cardInstanceSelectorService = cardInstanceSelectorService;
  }

  public abstract TriggerSubtype triggerSubtype();

  public void whenTriggered(GameStatus gameStatus, CardInstance cardInstance) {
    List<CardInstance> cardsWithTriggerAbility = gameStatus.getAllBattlefieldCards().withTriggerSubtype(triggerSubtype()).getCards();

    for (CardInstance cardWithTriggerAbility : cardsWithTriggerAbility) {
      for (CardInstanceAbility ability : cardWithTriggerAbility.getAbilitiesByTriggerSubType(triggerSubtype())) {
        CardInstanceSearch selectionForCardWithTriggeredAbility = cardInstanceSelectorService.select(gameStatus, cardWithTriggerAbility, ability.getTrigger().getCardInstanceSelector());
        if (selectionForCardWithTriggeredAbility.withId(cardInstance.getId()).isPresent()) {
          cardWithTriggerAbility.getTriggeredAbilities().add(ability);
        }
      }

      if (!cardWithTriggerAbility.getTriggeredAbilities().isEmpty()) {
        LOGGER.info("{} triggered {} because of {} {}.", cardInstance.getIdAndName(), triggerSubtype(), cardInstance.getIdAndName(), triggerSubtype());
        gameStatus.getStack().add(cardWithTriggerAbility);
      }
    }
  }
}

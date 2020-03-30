package com.matag.game.turn.action.enter;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.CardInstanceSearch;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class WhenEnterTheBattlefieldService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WhenEnterTheBattlefieldService.class);

  private final CardInstanceSelectorService cardInstanceSelectorService;

  void whenEnterTheBattlefield(GameStatus gameStatus, CardInstance cardInstance) {
    List<CardInstance> cardsWithEnterAbility = gameStatus.getAllBattlefieldCards().withTriggerSubtype(TriggerSubtype.WHEN_ENTER_THE_BATTLEFIELD).getCards();

    for (CardInstance cardWithEnterAbility : cardsWithEnterAbility) {
      for (CardInstanceAbility ability : cardWithEnterAbility.getAbilitiesByTriggerSubType(TriggerSubtype.WHEN_ENTER_THE_BATTLEFIELD)) {
        CardInstanceSearch selectionForCardWithEnterAbility = cardInstanceSelectorService.select(gameStatus, cardWithEnterAbility, ability.getTrigger().getCardInstanceSelector());
        if (selectionForCardWithEnterAbility.withId(cardInstance.getId()).isPresent()) {
          cardWithEnterAbility.getTriggeredAbilities().add(ability);
        }
      }

      if (!cardWithEnterAbility.getTriggeredAbilities().isEmpty()) {
        LOGGER.info("{} triggered {} because of {} entering the battlefield.", cardInstance.getIdAndName(), TriggerSubtype.WHEN_ENTER_THE_BATTLEFIELD, cardInstance.getIdAndName());
        gameStatus.getStack().add(cardWithEnterAbility);
      }
    }
  }
}

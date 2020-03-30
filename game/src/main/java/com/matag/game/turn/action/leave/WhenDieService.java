package com.matag.game.turn.action.leave;

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
public class WhenDieService {
  private static final Logger LOGGER = LoggerFactory.getLogger(WhenDieService.class);

  private final CardInstanceSelectorService cardInstanceSelectorService;

  void whenDie(GameStatus gameStatus, CardInstance cardInstance) {
    List<CardInstance> cardsWithDieAbility = gameStatus.getAllBattlefieldCards().withTriggerSubtype(TriggerSubtype.WHEN_DIE).getCards();

    for (CardInstance cardWithDieAbility : cardsWithDieAbility) {
      for (CardInstanceAbility ability : cardWithDieAbility.getAbilitiesByTriggerSubType(TriggerSubtype.WHEN_DIE)) {
        CardInstanceSearch selectionForCardWithEnterAbility = cardInstanceSelectorService.select(gameStatus, cardWithDieAbility, ability.getTrigger().getCardInstanceSelector());
        if (selectionForCardWithEnterAbility.withId(cardInstance.getId()).isPresent()) {
          cardWithDieAbility.getTriggeredAbilities().add(ability);
        }
      }

      if (!cardWithDieAbility.getTriggeredAbilities().isEmpty()) {
        LOGGER.info("{} triggered {} because of {} dying.", cardInstance.getIdAndName(), TriggerSubtype.WHEN_DIE, cardInstance.getIdAndName());
        gameStatus.getStack().add(cardWithDieAbility);
      }
    }
  }
}

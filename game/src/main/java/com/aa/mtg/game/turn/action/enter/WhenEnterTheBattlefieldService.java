package com.aa.mtg.game.turn.action.enter;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cardinstance.CardInstanceSearch;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.selection.CardInstanceSelectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aa.mtg.cards.ability.trigger.TriggerSubtype.WHEN_ENTER_THE_BATTLEFIELD;

@Component
public class WhenEnterTheBattlefieldService {

  private static final Logger LOGGER = LoggerFactory.getLogger(WhenEnterTheBattlefieldService.class);

  private final CardInstanceSelectorService cardInstanceSelectorService;

  public WhenEnterTheBattlefieldService(CardInstanceSelectorService cardInstanceSelectorService) {
    this.cardInstanceSelectorService = cardInstanceSelectorService;
  }

  void whenEnterTheBattlefield(GameStatus gameStatus, CardInstance cardInstance) {
    List<CardInstance> cardsWithEnterAbility = gameStatus.getAllBattlefieldCards().withTriggerSubtype(WHEN_ENTER_THE_BATTLEFIELD).getCards();

    for (CardInstance cardWithEnterAbility : cardsWithEnterAbility) {
      for (CardInstanceAbility ability : cardWithEnterAbility.getAbilitiesByTriggerSubType(WHEN_ENTER_THE_BATTLEFIELD)) {
        CardInstanceSearch selectionForCardWithEnterAbility = cardInstanceSelectorService.select(gameStatus, cardWithEnterAbility, ability.getTrigger().getCardInstanceSelector());
        if (selectionForCardWithEnterAbility.withId(cardInstance.getId()).isPresent()) {
          cardWithEnterAbility.getTriggeredAbilities().add(ability);
        }
      }

      if (!cardWithEnterAbility.getTriggeredAbilities().isEmpty()) {
        LOGGER.info("{} triggered {} because of {} entering the battlefield.", cardInstance.getIdAndName(), WHEN_ENTER_THE_BATTLEFIELD, cardInstance.getIdAndName());
        gameStatus.getStack().add(cardWithEnterAbility);
      }
    }
  }
}

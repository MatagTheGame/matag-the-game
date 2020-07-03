package com.matag.game.turn.action._continue;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.cost.CostService;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.cast.InstantSpeedService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.trigger.TriggerType.ACTIVATED_ABILITY;

@Component
@AllArgsConstructor
public class AutocontinueChecker {
  private final CostService costService;
  private final InstantSpeedService instantSpeedService;

  public boolean canPerformAnyAction(GameStatus gameStatus) {
    Player player = gameStatus.getActivePlayer();

    List<CardInstance> instants = new CardInstanceSearch(player.getHand().getCards()).withInstantSpeed().getCards();
    for (CardInstance cardInstance : instants) {
      if (costService.canAfford(cardInstance, null, gameStatus)) {
        return true;
      }
    }

    List<CardInstance> cards = player.getBattlefield().getCards();
    for (CardInstance cardInstance : cards) {
      List<CardInstanceAbility> cardAbilities = cardInstance.getAbilities();
      for (CardInstanceAbility cardAbility : cardAbilities) {
        if (cardAbility.getTrigger() != null && cardAbility.getTrigger().getType().equals(ACTIVATED_ABILITY)) {
          String ability = cardAbility.getAbilityType().toString();
          if (instantSpeedService.isAtInstantSpeed(cardInstance, ability)) {
            if (costService.canAfford(cardInstance, ability, gameStatus)) {
              return true;
            }
          }
        }
      }
    }

    if (gameStatus.getStack().search().notAcknowledged().isNotEmpty()) {
      return true;
    }


    // TODO Autocontinue main phases in future


    return false;
  }
}

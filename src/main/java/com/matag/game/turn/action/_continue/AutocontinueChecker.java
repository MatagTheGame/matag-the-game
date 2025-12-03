package com.matag.game.turn.action._continue;

import static com.matag.cards.ability.trigger.TriggerType.ACTIVATED_ABILITY;
import static com.matag.game.turn.phases.PhaseUtils.isMainPhase;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.cost.CostService;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.cast.InstantSpeedService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AutocontinueChecker {
  private final CostService costService;
  private final InstantSpeedService instantSpeedService;

  public boolean canPerformAnyAction(GameStatus gameStatus) {
    if (isMainPhase(gameStatus.getTurn().getCurrentPhase()) && gameStatus.isCurrentPlayerActive()) {
      return true;
    }

    if (gameStatus.getTurn().getInputRequiredAction() != null) {
      return true;
    }

    var player = gameStatus.getActivePlayer();

    var instants = player.getHand().search().withInstantSpeed().getCards();
    for (var instant : instants) {
      if (costService.canAfford(instant, null, gameStatus)) {
        return true;
      }
    }

    var cards = player.getBattlefield().getCards();
    for (var card : cards) {
      var cardAbilities = card.getAbilities();
      for (var cardAbility : cardAbilities) {
        if (cardAbility.getAbility().getTrigger() != null && cardAbility.getAbility().getTrigger().getType().equals(ACTIVATED_ABILITY)) {
          var ability = cardAbility.getAbility().getAbilityType().toString();
          if (instantSpeedService.isAtInstantSpeed(card, ability)) {
            if (costService.canAfford(card, ability, gameStatus)) {
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

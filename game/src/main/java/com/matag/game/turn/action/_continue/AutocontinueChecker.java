package com.matag.game.turn.action._continue;

import com.matag.cards.ability.type.AbilityType;
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

@Component
@AllArgsConstructor
public class AutocontinueChecker {
  private final CostService costService;
  private final InstantSpeedService instantSpeedService;

  public boolean canPerformAnyAction(GameStatus gameStatus) {
    Player player = gameStatus.getCurrentPlayer();

    List<CardInstance> instants = new CardInstanceSearch(player.getHand().getCards()).withInstantSpeed().getCards();
    for (CardInstance cardInstance : instants) {
      if (costService.canAfford(cardInstance, null, gameStatus)) {
        return true;
      }
    }

    List<CardInstance> cards = player.getBattlefield().getCards();
    for (CardInstance card : cards) {
      List<CardInstanceAbility> cardAbilities = card.getAbilities();
      for (CardInstanceAbility cardAbility : cardAbilities) {
        if (!cardAbility.getAbilityType().equals(AbilityType.TAP_ADD_MANA)) {
          if (instantSpeedService.isAtInstantSpeed(card, cardAbility.getAbilityType().toString())) {
            return true;
          }
        }
      }
    }


    // TODO Autocontinue main phases in future


    return false;
  }
}

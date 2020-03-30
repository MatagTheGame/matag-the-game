package com.matag.game.turn.action.cast;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.cost.CostService;
import com.matag.cards.properties.Cost;
import com.matag.game.message.MessageException;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.Turn;
import com.matag.game.turn.action.tap.TapPermanentService;
import com.matag.game.turn.action.target.TargetCheckerService;
import com.matag.game.turn.phases.PhaseUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class CastService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CastService.class);

  private final TargetCheckerService targetCheckerService;
  private final ManaCountService manaCountService;
  private final TapPermanentService tapPermanentService;
  private final CostService costService;

  public void cast(GameStatus gameStatus, int cardId, Map<Integer, List<String>> mana, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
    Turn turn = gameStatus.getTurn();
    Player activePlayer = gameStatus.getActivePlayer();

    CardInstance cardToCast;
    String castedFrom;
    if (activePlayer.getHand().hasCardById(cardId)) {
      cardToCast = activePlayer.getHand().findCardById(cardId);
      castedFrom = "HAND";
    } else {
      cardToCast = activePlayer.getBattlefield().findCardById(cardId);
      castedFrom = "BATTLEFIELD";
    }

    if (!PhaseUtils.isMainPhase(turn.getCurrentPhase()) && !cardToCast.isInstantSpeed()) {
      throw new MessageException("You can only play Instants during a NON main phases.");

    } else {
      checkSpellOrAbilityCost(mana, activePlayer, cardToCast, playedAbility);
      targetCheckerService.checkSpellOrAbilityTargetRequisites(cardToCast, gameStatus, targetsIdsForCardIds, playedAbility);

      if (castedFrom.equals("HAND")) {
        activePlayer.getHand().extractCardById(cardId);
        cardToCast.setController(activePlayer.getName());
        gameStatus.getStack().add(cardToCast);

      } else {
        CardInstanceAbility triggeredAbility = cardToCast.getAbilities().get(0);
        cardToCast.getTriggeredAbilities().add(triggeredAbility);
        LOGGER.info("Player {} triggered ability {} for {}.", activePlayer.getName(), triggeredAbility.getAbilityType(), cardToCast.getModifiers());
        gameStatus.getStack().add(cardToCast);
      }

      gameStatus.getTurn().passPriority(gameStatus);

      // FIXME Antonio: Do not tap all lands but only the one necessary to pay the cost above. If not player may lose some mana if miscalculated.
      mana.keySet().stream()
        .map(cardInstanceId -> activePlayer.getBattlefield().findCardById(cardInstanceId))
        .forEach(card -> tapPermanentService.tap(gameStatus, card.getId()));
      gameStatus.getTurn().setLastManaPaid(mana);
    }
  }

  private void checkSpellOrAbilityCost(Map<Integer, List<String>> mana, Player currentPlayer, CardInstance cardToCast, String ability) {
    List<Cost> paidCost = manaCountService.verifyManaPaid(mana, currentPlayer);
    if (!costService.isCastingCostFulfilled(cardToCast.getCard(), paidCost, ability)) {
      throw new MessageException("There was an error while paying the cost for " + cardToCast.getIdAndName() + ".");
    }
  }


}

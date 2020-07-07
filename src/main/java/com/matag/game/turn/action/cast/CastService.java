package com.matag.game.turn.action.cast;

import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.Cost;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.cost.CostService;
import com.matag.game.cardinstance.cost.PayCostService;
import com.matag.game.message.MessageException;
import com.matag.game.player.Player;
import com.matag.game.stack.SpellType;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.Turn;
import com.matag.game.turn.action.target.TargetCheckerService;
import com.matag.game.turn.action.trigger.WhenTriggerService;
import com.matag.game.turn.phases.PhaseUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.matag.cards.ability.trigger.TriggerSubtype.WHEN_CAST;
import static com.matag.game.stack.SpellType.ABILITY;
import static com.matag.game.stack.SpellType.SPELL;

@Component
@AllArgsConstructor
public class CastService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CastService.class);

  private final TargetCheckerService targetCheckerService;
  private final ManaCountService manaCountService;
  private final CostService costService;
  private final PayCostService payCostService;
  private final InstantSpeedService instantSpeedService;
  private final WhenTriggerService whenTriggerService;

  public void cast(GameStatus gameStatus, int cardId, Map<Integer, List<String>> mana, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
    var turn = gameStatus.getTurn();
    var activePlayer = gameStatus.getActivePlayer();

    CardInstance cardToCast;
    SpellType spellType;
    if (activePlayer.getHand().hasCardById(cardId)) {
      cardToCast = activePlayer.getHand().findCardById(cardId);
      spellType = SPELL;
    } else {
      cardToCast = activePlayer.getBattlefield().findCardById(cardId);
      spellType = ABILITY;
    }

    if (!PhaseUtils.isMainPhase(turn.getCurrentPhase()) && !instantSpeedService.isAtInstantSpeed(cardToCast, playedAbility)) {
      throw new MessageException(cardToCast.getIdAndName() + (playedAbility == null ? "'s ability" : "") + " cannot be cast at instant speed.");

    } else {
      checkSpellOrAbilityCost(mana, activePlayer, cardToCast, playedAbility);
      targetCheckerService.checkSpellOrAbilityTargetRequisites(cardToCast, gameStatus, targetsIdsForCardIds, playedAbility);

      if (spellType.equals(SPELL)) {
        activePlayer.getHand().extractCardById(cardId);
        cardToCast.setController(activePlayer.getName());
        gameStatus.getStack().add(cardToCast);
        whenTriggerService.whenTriggered(gameStatus, cardToCast, WHEN_CAST);

      } else {
        var triggeredAbility = cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility)).get(0);
        cardToCast.getTriggeredAbilities().add(triggeredAbility);
        LOGGER.info("Player {} triggered ability {} for {}.", activePlayer.getName(), triggeredAbility.getAbilityType(), cardToCast.getModifiers());
        gameStatus.getStack().add(cardToCast);
      }

      payCostService.pay(gameStatus, activePlayer, cardToCast, playedAbility, mana);
      gameStatus.getTurn().passPriority(gameStatus);
    }
  }

  private void checkSpellOrAbilityCost(Map<Integer, List<String>> mana, Player currentPlayer, CardInstance cardToCast, String ability) {
    var paidCost = manaCountService.verifyManaPaid(mana, currentPlayer);
    if (!costService.isCastingCostFulfilled(cardToCast, ability, paidCost)) {
      throw new MessageException("There was an error while paying the cost for " + cardToCast.getIdAndName() + ".");
    }
  }


}

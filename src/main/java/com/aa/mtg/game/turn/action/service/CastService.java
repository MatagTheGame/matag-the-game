package com.aa.mtg.game.turn.action.service;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CostUtils;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.PhaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CastService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CastService.class);

    private final TargetCheckerService targetCheckerService;
    private final ManaCountService manaCountService;

    @Autowired
    public CastService(TargetCheckerService targetCheckerService, ManaCountService manaCountService) {
        this.targetCheckerService = targetCheckerService;
        this.manaCountService = manaCountService;
    }

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

        if (!PhaseUtils.isMainPhase(turn.getCurrentPhase()) && !cardToCast.getCard().isInstantSpeed()) {
            throw new MessageException("You can only play Instants during a NON main phases.");

        } else {
            checkSpellOrAbilityCost(mana, activePlayer, cardToCast, playedAbility);
            targetCheckerService.checkSpellOrAbilityTargetRequisites(cardToCast, gameStatus, targetsIdsForCardIds, playedAbility);

            if (castedFrom.equals("HAND")) {
                activePlayer.getHand().extractCardById(cardId);
                cardToCast.setController(activePlayer.getName());
                gameStatus.getStack().add(cardToCast);

            } else {
                Ability triggeredAbility = cardToCast.getAbilities().get(0);
                cardToCast.getTriggeredAbilities().add(triggeredAbility);
                LOGGER.info("Player {} triggered ability {} for {}.", activePlayer.getName(), triggeredAbility.getAbilityTypes(), cardToCast.getModifiers());
                gameStatus.getStack().add(cardToCast);
            }

            gameStatus.getTurn().passPriority(gameStatus);

            // FIXME Antonio: Do not tap all lands but only the one necessary to pay the cost above. If not player may lose some mana if miscalculated.
            mana.keySet().stream()
                    .map(cardInstanceId -> activePlayer.getBattlefield().findCardById(cardInstanceId))
                    .forEach(card -> card.getModifiers().tap());
        }
    }

    private void checkSpellOrAbilityCost(Map<Integer, List<String>> mana, Player currentPlayer, CardInstance cardToCast, String ability) {
        List<Cost> paidCost = manaCountService.verifyManaPaid(mana, currentPlayer);
        if (!CostUtils.isCastingCostFulfilled(cardToCast.getCard(), paidCost, ability)) {
            throw new MessageException("There was an error while paying the cost for " + cardToCast.getIdAndName() + ".");
        }
    }


}

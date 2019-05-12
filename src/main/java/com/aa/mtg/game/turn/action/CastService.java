package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CostUtils;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.ability.action.AbilityActionFactory;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.PhaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CastService {

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final AbilityActionFactory abilityActionFactory;

    @Autowired
    public CastService(GameStatusUpdaterService gameStatusUpdaterService, AbilityActionFactory abilityActionFactory) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.abilityActionFactory = abilityActionFactory;
    }

    public void cast(GameStatus gameStatus, int cardId, List<Integer> tappingLandIds, Map<Integer, List<Object>> targetsIdsForCardIds) {
        Turn turn = gameStatus.getTurn();
        Player currentPlayer = gameStatus.getCurrentPlayer();

        CardInstance cardToCast = currentPlayer.getHand().findCardById(cardId);
        if (!PhaseUtils.isMainPhase(turn.getCurrentPhase()) && !cardToCast.getCard().isInstantSpeed()) {
            throw new MessageException("You can only play Instants during a NON main phases.");

        } else {
            checkSpellCost(tappingLandIds, currentPlayer, cardToCast);
            checkSpellRequisites(cardToCast, gameStatus, targetsIdsForCardIds);

            currentPlayer.getHand().extractCardById(cardId);
            cardToCast.setController(currentPlayer.getName());
            gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);

            gameStatus.getStack().addLast(cardToCast);
            gameStatusUpdaterService.sendUpdateStack(gameStatus);

            gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

            // FIXME Do not tap all lands but only the one necessary to pay the cost above. If not player may lose some mana if miscalculated.
            tappingLandIds.stream()
                    .map(tappingLandId -> currentPlayer.getBattlefield().findCardById(tappingLandId))
                    .forEach(card -> card.getModifiers().tap());
            gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
        }
    }

    private void checkSpellCost(List<Integer> tappingLandIds, Player currentPlayer, CardInstance cardToCast) {
        ArrayList<Color> paidCost = new ArrayList<>();
        for (int tappingLandId : tappingLandIds) {
            CardInstance landToTap = currentPlayer.getBattlefield().findCardById(tappingLandId);
            if (!landToTap.isOfType(Type.LAND)) {
                throw new MessageException("The card you are trying to tap for mana is not a land.");
            } else if (landToTap.getModifiers().isTapped()) {
                throw new MessageException("The land you are trying to tap is already tapped.");
            }
            paidCost.add(landToTap.getCard().getColors().get(0));
        }

        if (!CostUtils.isCastingCostFulfilled(cardToCast.getCard(), paidCost)) {
            throw new MessageException("There was an error while paying the cost for " + cardToCast.getIdAndName() + ".");
        }
    }

    private void checkSpellRequisites(CardInstance cardToCast, GameStatus gameStatus, Map<Integer, List<Object>> targetsIdsForCardIds) {
        for (Ability ability : cardToCast.getAbilities()) {
            AbilityAction abilityAction = abilityActionFactory.getAbilityAction(ability.getMainAbilityType());
            if (abilityAction != null) {
                if (targetsIdsForCardIds == null || !targetsIdsForCardIds.containsKey(cardToCast.getId()) || targetsIdsForCardIds.get(cardToCast.getId()).isEmpty()) {
                    throw new MessageException(cardToCast.getIdAndName() + " requires a valid target.");
                }

                List<Object> targetIds = targetsIdsForCardIds.get(cardToCast.getId());
                for (int i = 0; i < ability.getTargets().size(); i++) {
                    ability.getTargets().get(i).check(gameStatus, targetIds.get(i));
                }

                cardToCast.getModifiers().setTargets(targetIds);
            }
        }
    }
}

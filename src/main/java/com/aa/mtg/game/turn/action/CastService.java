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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.ability.type.AbilityType.abilityType;

@Service
public class CastService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CastService.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;
    private final AbilityActionFactory abilityActionFactory;

    @Autowired
    public CastService(GameStatusUpdaterService gameStatusUpdaterService, AbilityActionFactory abilityActionFactory) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
        this.abilityActionFactory = abilityActionFactory;
    }

    public void cast(GameStatus gameStatus, int cardId, List<Integer> tappingLandIds, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
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
            checkSpellOrAbilityCost(tappingLandIds, activePlayer, cardToCast, playedAbility);
            checkSpellOrAbilityTargetRequisites(cardToCast, gameStatus, targetsIdsForCardIds, playedAbility);

            if (castedFrom.equals("HAND")) {
                activePlayer.getHand().extractCardById(cardId);
                cardToCast.setController(activePlayer.getName());
                gameStatusUpdaterService.sendUpdatePlayerHand(gameStatus, activePlayer);

                gameStatus.getStack().add(cardToCast);
                gameStatusUpdaterService.sendUpdateStack(gameStatus);

            } else {
                Ability triggeredAbility = cardToCast.getAbilities().get(0);
                cardToCast.getTriggeredAbilities().add(triggeredAbility);
                LOGGER.info("Player {} triggered ability {} for {}.", activePlayer.getName(), triggeredAbility.getAbilityTypes(), cardToCast.getModifiers());
                gameStatus.getStack().add(cardToCast);
                gameStatusUpdaterService.sendUpdateStack(gameStatus);
            }

            gameStatus.getTurn().passPriority(gameStatus);
            gameStatusUpdaterService.sendUpdateTurn(gameStatus);

            // FIXME Do not tap all lands but only the one necessary to pay the cost above. If not player may lose some mana if miscalculated.
            tappingLandIds.stream()
                    .map(tappingLandId -> activePlayer.getBattlefield().findCardById(tappingLandId))
                    .forEach(card -> card.getModifiers().tap());
            gameStatusUpdaterService.sendUpdatePlayerBattlefield(gameStatus, activePlayer);
        }
    }

    private void checkSpellOrAbilityCost(List<Integer> tappingLandIds, Player currentPlayer, CardInstance cardToCast, String ability) {
        ArrayList<Color> paidCost = getManaPaid(tappingLandIds, currentPlayer);
        if (!CostUtils.isCastingCostFulfilled(cardToCast.getCard(), paidCost, ability)) {
            throw new MessageException("There was an error while paying the cost for " + cardToCast.getIdAndName() + ".");
        }
    }

    private ArrayList<Color> getManaPaid(List<Integer> tappingLandIds, Player currentPlayer) {
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
        return paidCost;
    }

    private void checkSpellOrAbilityTargetRequisites(CardInstance cardToCast, GameStatus gameStatus, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
        for (Ability ability : cardToCast.getAbilities()) {
            AbilityAction abilityAction = abilityActionFactory.getAbilityAction(abilityType(playedAbility));
            if (abilityAction != null &&  ability.requiresTarget()) {
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

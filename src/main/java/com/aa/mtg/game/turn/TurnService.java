package com.aa.mtg.game.turn;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CostUtils;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TurnService {

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public TurnService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    void continueTurn(GameStatus gameStatus) {
        Turn turn = gameStatus.getTurn();
        Player activePlayer = gameStatus.getActivePlayer();
        Player inactivePlayer = gameStatus.getInactivePlayer();

        if (turn.getCurrentPhase().equals(Phase.UT)) {
            activePlayer.getBattlefield().getCards().stream()
                    .filter(cardInstance -> cardInstance.getModifiers().isTapped())
                    .forEach(cardInstance -> cardInstance.getModifiers().untap());

            gameStatusUpdaterService.sendUpdateActivePlayerBattlefield(gameStatus);
            turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));

        } else if (turn.getCurrentPhase().equals(Phase.CL)) {
            turn.cleanup(inactivePlayer.getName());

        } else if (turn.getCurrentPhase().equals(Phase.ET) && activePlayer.getHand().size() > 7) {
            gameStatus.getTurn().setTriggeredAction("DISCARD_A_CARD");

        } else {
            if (turn.getCurrentPhaseActivePlayer().equals(activePlayer.getName())) {
                if (turn.getCurrentPhase().equals(Phase.DR) && turn.getTurnNumber() > 1) {
                    CardInstance cardInstance = activePlayer.getLibrary().draw();
                    activePlayer.getHand().addCard(cardInstance);
                    gameStatusUpdaterService.sendUpdateActivePlayerHand(gameStatus);
                }

                if (Phase.nonOpponentPhases().contains(turn.getCurrentPhase())) {
                    turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
                } else {
                    turn.setCurrentPhaseActivePlayer(inactivePlayer.getName());
                }

            } else {
                turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
                turn.setCurrentPhaseActivePlayer(activePlayer.getName());
            }
        }

        gameStatusUpdaterService.sendUpdateTurn(gameStatus);
    }

    void playLand(GameStatus gameStatus, int cardId) {
        Turn turn = gameStatus.getTurn();
        Player activePlayer = gameStatus.getActivePlayer();

        if (!turn.getCurrentPhase().isMainPhase()) {
            gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "You can only play lands during main phases.");

        } else if (turn.getCardsPlayedWithinTurn().stream()
                .map(CardInstance::getCard)
                .map(Card::getTypes)
                .anyMatch(types -> types.contains(Type.LAND))) {
            gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "You already played a land this turn.");

        } else {
            CardInstance cardInstance = activePlayer.getHand().extractCardById(cardId);
            turn.addCardToCardsPlayedWithinTurn(cardInstance);
            activePlayer.getBattlefield().addCard(cardInstance);

            gameStatusUpdaterService.sendUpdateActivePlayerBattlefield(gameStatus);
            gameStatusUpdaterService.sendUpdateActivePlayerHand(gameStatus);
        }
    }

    void cast(GameStatus gameStatus, int cardId, List<Integer> tappingLandIds) {
        Turn turn = gameStatus.getTurn();
        Player activePlayer = gameStatus.getActivePlayer();

        CardInstance cardInstance = activePlayer.getHand().findCardById(cardId);
        if (!turn.getCurrentPhase().isMainPhase() && !cardInstance.getCard().isInstantSpeed()) {
            gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "You can only play Instants during a NON main phases.");

        } else {
            CardInstance cardToCast = activePlayer.getHand().findCardById(cardId);

            ArrayList<Color> paidCost = new ArrayList<>();
            for (int tappingLandId : tappingLandIds) {
                CardInstance landToTap = activePlayer.getBattlefield().findCardById(tappingLandId);
                if (!landToTap.getCard().getTypes().contains(Type.LAND)) {
                    gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "The card you are trying to tap for mana is not a land.");
                } else if (landToTap.getModifiers().isTapped()) {
                    gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "The land you are trying to tap is already tapped.");
                }
                paidCost.add(landToTap.getCard().getColors().get(0));
            }

            if (!CostUtils.isCastingCostFulfilled(cardToCast.getCard(), paidCost)) {
                gameStatusUpdaterService.sendMessageToActivePlayer(activePlayer, "There was an error while paying the cost for " + cardToCast.getCard().getName() + ".");

            } else {
                cardInstance = activePlayer.getHand().extractCardById(cardId);
                activePlayer.getBattlefield().addCard(cardInstance);

                // TODO do not tap all lands but only the one necessary to pay the cost above... this gets complicated
                tappingLandIds.stream()
                        .map(tappingLandId -> activePlayer.getBattlefield().findCardById(tappingLandId))
                        .forEach(card -> card.getModifiers().tap());

                gameStatusUpdaterService.sendUpdateActivePlayerBattlefield(gameStatus);
                gameStatusUpdaterService.sendUpdateActivePlayerHand(gameStatus);
            }
        }
    }

    void resolve(GameStatus gameStatus, String triggeredAction, int cardId) {
        if (gameStatus.getTurn().getTriggeredAction().equals(triggeredAction)) {
            if ("DISCARD_A_CARD".equals(triggeredAction)) {
                CardInstance cardInstance = gameStatus.getActivePlayer().getHand().extractCardById(cardId);
                gameStatus.getActivePlayer().getGraveyard().addCard(cardInstance);
                gameStatusUpdaterService.sendUpdateActivePlayerHand(gameStatus);
                gameStatusUpdaterService.sendUpdateActivePlayerGraveyard(gameStatus);
                gameStatus.getTurn().setTriggeredAction(null);
            }
            continueTurn(gameStatus);

        } else {
            String message = "Cannot resolve triggeredAction " + triggeredAction + " as current triggeredAction is " + gameStatus.getTurn().getTriggeredAction();
            gameStatusUpdaterService.sendMessageToActivePlayer(gameStatus.getActivePlayer(), message);
            throw new RuntimeException(message);
        }
    }
}

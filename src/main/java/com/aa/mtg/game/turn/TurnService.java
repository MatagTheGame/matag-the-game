package com.aa.mtg.game.turn;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CostUtils;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.phases.Phase;
import com.aa.mtg.message.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Service
public class TurnService {

    private final EventSender eventSender;

    @Autowired
    public TurnService(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    void continueTurn(GameStatus gameStatus) {
        Turn turn = gameStatus.getTurn();
        Player activePlayer = gameStatus.getActivePlayer();
        Player inactivePlayer = gameStatus.getInactivePlayer();


        if (turn.getCurrentPhase().equals(Phase.CL)) {
            turn.cleanup(inactivePlayer.getName());

        } else if (turn.getCurrentPhase().equals(Phase.ET) && activePlayer.getHand().size() > 7) {
            gameStatus.getTurn().setTriggeredAction("DISCARD_A_CARD");

        } else {
            if (turn.getCurrentPhaseActivePlayer().equals(activePlayer.getName())) {
                if (turn.getCurrentPhase().equals(Phase.DR) && turn.getTurnNumber() > 1) {
                    CardInstance cardInstance = activePlayer.getLibrary().draw();
                    activePlayer.getHand().addCard(cardInstance);
                    updatePlayerHands(activePlayer, inactivePlayer);
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

        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", gameStatus.getTurn())
        );
    }

    void playLand(GameStatus gameStatus, int cardId) {
        Turn turn = gameStatus.getTurn();

        if (!turn.getCurrentPhase().isMainPhase()) {
            eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("MESSAGE", new MessageEvent("You can only play lands during main phases.", true)));

        } else if (turn.getCardsPlayedWithinTurn().stream()
                .map(CardInstance::getCard)
                .map(Card::getTypes)
                .anyMatch(types -> types.contains(Type.LAND))) {
            eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("MESSAGE", new MessageEvent("You already played a land this turn.", true)));

        } else {
            CardInstance cardInstance = gameStatus.getActivePlayer().getHand().extractCardById(cardId);
            turn.addCardToCardsPlayedWithinTurn(cardInstance);
            gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance);

            eventSender.sendToPlayers(
                    asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                    new Event("UPDATE_ACTIVE_PLAYER_BATTLEFIELD", gameStatus.getActivePlayer().getBattlefield().getCards())
            );

            updatePlayerHands(gameStatus.getActivePlayer(), gameStatus.getInactivePlayer());
        }
    }

    void cast(GameStatus gameStatus, int cardId, List<Integer> tappingLandIds) {
        Turn turn = gameStatus.getTurn();

        CardInstance cardInstance = gameStatus.getActivePlayer().getHand().extractCardById(cardId);
        if (!turn.getCurrentPhase().isMainPhase() && !cardInstance.getCard().isInstantSpeed()) {
            eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("MESSAGE", new MessageEvent("You can only play Instants during a NON main phases.", true)));

        } else {
            CardInstance cardToCast = gameStatus.getActivePlayer().getHand().findCardById(cardId);
            ArrayList<Color> paidCost = new ArrayList<>();

            for (int tappingLandId : tappingLandIds) {
                CardInstance landToTap = gameStatus.getActivePlayer().getBattlefield().findCardById(tappingLandId);
                if (!landToTap.getCard().getTypes().contains(Type.LAND)) {
                    eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("MESSAGE", new MessageEvent("The card you are trying to tap for mana is not a land.", true)));
                } else if (landToTap.getModifiers().isTapped()) {
                    eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("MESSAGE", new MessageEvent("The land you are trying to tap is already tapped.", true)));
                }
                paidCost.add(landToTap.getCard().getColors().get(0));
            }

            if (!CostUtils.isCastingCostFulfilled(cardToCast.getCard(), paidCost)) {
                eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("MESSAGE", new MessageEvent("There was an error while paying the cost for " + cardToCast.getCard().getName() + ".", true)));
            }

            tappingLandIds.stream()
                    .map(tappingLandId -> gameStatus.getActivePlayer().getBattlefield().findCardById(tappingLandId))
                    .forEach(card -> card.getModifiers().tap());

            eventSender.sendToPlayers(
                    asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                    new Event("UPDATE_ACTIVE_PLAYER_BATTLEFIELD", gameStatus.getActivePlayer().getBattlefield().getCards())
            );
        }
    }

    void resolve(GameStatus gameStatus, String triggeredAction, int cardId) {
        if (gameStatus.getTurn().getTriggeredAction().equals(triggeredAction)) {
            if ("DISCARD_A_CARD".equals(triggeredAction)) {
                CardInstance cardInstance = gameStatus.getActivePlayer().getHand().extractCardById(cardId);
                gameStatus.getActivePlayer().getGraveyard().addCard(cardInstance);
                updatePlayerHands(gameStatus.getActivePlayer(), gameStatus.getInactivePlayer());

                eventSender.sendToPlayers(
                        asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                        new Event("UPDATE_ACTIVE_PLAYER_GRAVEYARD", gameStatus.getActivePlayer().getGraveyard().getCards())
                );
                gameStatus.getTurn().setTriggeredAction(null);
            }
            continueTurn(gameStatus);

        } else {
            String message = "Cannot resolve triggeredAction " + triggeredAction + " as current triggeredAction is " + gameStatus.getTurn().getTriggeredAction();
            eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event(message, true));
            throw new RuntimeException(message);
        }
    }

    private void updatePlayerHands(Player activePlayer, Player inactivePlayer) {
        eventSender.sendToPlayer(activePlayer, new Event("UPDATE_ACTIVE_PLAYER_HAND", activePlayer.getHand().getCards()));
        eventSender.sendToPlayer(inactivePlayer, new Event("UPDATE_ACTIVE_PLAYER_HAND", activePlayer.getHand().maskedHand()));
    }
}

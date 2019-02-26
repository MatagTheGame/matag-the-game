package com.aa.mtg.game.turn;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.phases.Phase;
import com.aa.mtg.message.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Arrays.asList;

@Service
public class TurnService {

    private final EventSender eventSender;

    @Autowired
    public TurnService(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    public void continueTurn(GameStatus gameStatus) {
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
                    eventSender.sendToPlayer(activePlayer, new Event("UPDATE_ACTIVE_PLAYER_HAND", activePlayer.getHand().getCards()));
                    eventSender.sendToPlayer(inactivePlayer, new Event("UPDATE_ACTIVE_PLAYER_HAND", activePlayer.getHand().maskedHand()));
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

    public void playLand(GameStatus gameStatus, int cardId) {
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
                    new Event("UPDATE_ACTIVE_PLAYER_BATTLEFIELD", gameStatus.getActivePlayer().getBattlefield())
            );

            eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getActivePlayer().getHand().getCards()));
            eventSender.sendToPlayer(gameStatus.getInactivePlayer(), new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getActivePlayer().getHand().maskedHand()));
        }
    }

    public void resolve(GameStatus gameStatus, String triggeredAction, int cardId) {
        if (gameStatus.getTurn().getTriggeredAction().equals(triggeredAction)) {
            if ("DISCARD_A_CARD".equals(triggeredAction)) {
                CardInstance cardInstance = gameStatus.getActivePlayer().getHand().extractCardById(cardId);
                gameStatus.getActivePlayer().getGraveyard().addCard(cardInstance);
                eventSender.sendToPlayers(
                        asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                        new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getActivePlayer().getHand().getCards())
                );
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
}

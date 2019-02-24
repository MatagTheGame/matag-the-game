package com.aa.mtg.game.turn;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.phases.Phase;
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
        updateToNextTurn(turn, gameStatus.getActivePlayer(), gameStatus.getInactivePlayer());
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", gameStatus.getTurn())
        );
    }

    public void playLand(GameStatus gameStatus, int cardId) {
        Turn turn = gameStatus.getTurn();

        boolean alreadyPlayedALand = turn.getCardsPlayedWithinTurn().stream()
                .map(CardInstance::getCard)
                .map(Card::getTypes)
                .anyMatch(types -> types.contains(Type.LAND));

        if (!alreadyPlayedALand) {
            CardInstance cardInstance = gameStatus.getActivePlayer().getHand().extractCardById(cardId);
            gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance);

            eventSender.sendToPlayers(
                    asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                    new Event("UPDATE_ACTIVE_PLAYER_BATTLEFIELD", gameStatus.getActivePlayer().getBattlefield())
            );

        } else {
            throw new RuntimeException("ERROR LAND ALREADY PLAYED");
        }
    }

    private void updateToNextTurn(Turn turn, Player player, Player opponent) {
        if (turn.getCurrentPhaseActivePlayer().equals(player.getName())) {
            if (Phase.nonOpponentPhases().contains(turn.getCurrentPhase())) {
                turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
            } else {
                turn.setCurrentPhaseActivePlayer(opponent.getName());
            }

        } else {
            turn.setCurrentPhase(Phase.nextPhase(turn.getCurrentPhase()));
            turn.setCurrentPhaseActivePlayer(player.getName());
        }
    }
}

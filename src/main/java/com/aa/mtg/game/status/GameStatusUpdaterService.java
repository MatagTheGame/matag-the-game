package com.aa.mtg.game.status;

import com.aa.mtg.game.event.Event;
import com.aa.mtg.game.event.EventSender;
import com.aa.mtg.game.message.MessageEvent;
import com.aa.mtg.game.player.Graveyard;
import com.aa.mtg.game.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Arrays.asList;

@Service
public class GameStatusUpdaterService {

    private final EventSender eventSender;

    @Autowired
    public GameStatusUpdaterService(EventSender eventSender) {
        this.eventSender = eventSender;
    }

    public void sendUpdateTurn(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", gameStatus.getTurn())
        );
    }

    public void sendUpdateActivePlayerHand(GameStatus gameStatus) {
        eventSender.sendToPlayer(gameStatus.getActivePlayer(), new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getActivePlayer().getHand().getCards()));
        eventSender.sendToPlayer(gameStatus.getInactivePlayer(), new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getActivePlayer().getHand().maskedHand()));
    }

    public void sendUpdateActivePlayerBattlefield(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_ACTIVE_PLAYER_BATTLEFIELD", gameStatus.getActivePlayer().getBattlefield().getCards())
        );
    }

    public void sendUpdateActivePlayerGraveyard(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_ACTIVE_PLAYER_GRAVEYARD", gameStatus.getActivePlayer().getGraveyard().getCards())
        );
    }

    public void sendMessageToActivePlayer(Player activePlayer, String message) {
        eventSender.sendToPlayer(activePlayer, new Event("MESSAGE", new MessageEvent(message, true)));
    }
}

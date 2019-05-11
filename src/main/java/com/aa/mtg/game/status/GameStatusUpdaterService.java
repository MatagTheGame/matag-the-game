package com.aa.mtg.game.status;

import com.aa.mtg.game.event.Event;
import com.aa.mtg.game.event.EventSender;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.turn.events.LifeEvent;
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

    public void sendUpdateCurrentPlayerLibrarySize(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_ACTIVE_PLAYER_LIBRARY_SIZE", gameStatus.getCurrentPlayer().getLibrary().size())
        );
    }

    public void sendUpdateNonCurrentPlayerLibrarySize(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_INACTIVE_PLAYER_LIBRARY_SIZE", gameStatus.getNonCurrentPlayer().getLibrary().size())
        );
    }

    public void sendUpdatePlayerLibrarySize(GameStatus gameStatus, Player player) {
        if (gameStatus.isPlayerCurrent(player)) {
            sendUpdateCurrentPlayerLibrarySize(gameStatus);
        } else {
            sendUpdateNonCurrentPlayerLibrarySize(gameStatus);
        }
    }

    public void sendUpdateCurrentPlayerHand(GameStatus gameStatus) {
        eventSender.sendToPlayer(gameStatus.getCurrentPlayer(), new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getCurrentPlayer().getHand().getCards()));
        eventSender.sendToPlayer(gameStatus.getNonCurrentPlayer(), new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getCurrentPlayer().getHand().maskedHand()));
    }

    public void sendUpdateNonCurrentPlayerHand(GameStatus gameStatus) {
        eventSender.sendToPlayer(gameStatus.getNonCurrentPlayer(), new Event("UPDATE_INACTIVE_PLAYER_HAND", gameStatus.getNonCurrentPlayer().getHand().getCards()));
        eventSender.sendToPlayer(gameStatus.getCurrentPlayer(), new Event("UPDATE_INACTIVE_PLAYER_HAND", gameStatus.getNonCurrentPlayer().getHand().maskedHand()));
    }

    public void sendUpdatePlayerHand(GameStatus gameStatus, Player player) {
        if (gameStatus.isPlayerCurrent(player)) {
            sendUpdateCurrentPlayerHand(gameStatus);
        } else {
            sendUpdateNonCurrentPlayerHand(gameStatus);
        }
    }

    public void sendUpdateCurrentPlayerBattlefield(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_ACTIVE_PLAYER_BATTLEFIELD", gameStatus.getCurrentPlayer().getBattlefield().getCards())
        );
    }

    public void sendUpdateNonCurrentPlayerBattlefield(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_INACTIVE_PLAYER_BATTLEFIELD", gameStatus.getNonCurrentPlayer().getBattlefield().getCards())
        );
    }

    public void sendUpdatePlayerBattlefield(GameStatus gameStatus, Player player) {
        if (gameStatus.isPlayerCurrent(player)) {
            sendUpdateCurrentPlayerBattlefield(gameStatus);
        } else {
            sendUpdateNonCurrentPlayerBattlefield(gameStatus);
        }
    }

    public void sendUpdateBattlefields(GameStatus gameStatus) {
        sendUpdateCurrentPlayerBattlefield(gameStatus);
        sendUpdateNonCurrentPlayerBattlefield(gameStatus);
    }

    public void sendUpdateCurrentPlayerGraveyard(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_ACTIVE_PLAYER_GRAVEYARD", gameStatus.getCurrentPlayer().getGraveyard().getCards())
        );
    }

    public void sendUpdateNonCurrentPlayerGraveyard(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_INACTIVE_PLAYER_GRAVEYARD", gameStatus.getNonCurrentPlayer().getGraveyard().getCards())
        );
    }

    public void sendUpdatePlayerGraveyard(GameStatus gameStatus, Player player) {
        if (gameStatus.isPlayerCurrent(player)) {
            sendUpdateCurrentPlayerGraveyard(gameStatus);
        } else {
            sendUpdateNonCurrentPlayerGraveyard(gameStatus);
        }
    }

    public void sendUpdateGraveyards(GameStatus gameStatus) {
        sendUpdateCurrentPlayerGraveyard(gameStatus);
        sendUpdateNonCurrentPlayerGraveyard(gameStatus);
    }

    public void sendUpdatePlayerLife(GameStatus gameStatus, Player player) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_PLAYER_LIFE", new LifeEvent(player.getName(), player.getLife()))
        );
    }

    public void sendUpdateStack(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_STACK", gameStatus.getStack())
        );
    }
}

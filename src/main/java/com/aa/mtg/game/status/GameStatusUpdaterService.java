package com.aa.mtg.game.status;

import com.aa.mtg.game.event.Event;
import com.aa.mtg.game.event.EventSender;
import com.aa.mtg.game.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;

@Component
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

    public void sendUpdatePlayerLibrarySize(GameStatus gameStatus, Player player) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_PLAYER_LIBRARY_SIZE", player, player.getLibrary().size())
        );
    }

    public void sendUpdatePlayerHand(GameStatus gameStatus, Player player) {
        Player otherPlayer = gameStatus.getOtherPlayer(player);
        eventSender.sendToPlayer(player, new Event("UPDATE_PLAYER_HAND", player, player.getHand().getCards()));
        eventSender.sendToPlayer(otherPlayer, new Event("UPDATE_PLAYER_HAND", player, player.getHand().maskedHand()));
    }

    public void sendUpdatePlayerBattlefield(GameStatus gameStatus, Player player) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_PLAYER_BATTLEFIELD", player, player.getBattlefield().getCards())
        );
    }

    public void sendUpdateBattlefields(GameStatus gameStatus) {
        sendUpdatePlayerBattlefield(gameStatus, gameStatus.getPlayer1());
        sendUpdatePlayerBattlefield(gameStatus, gameStatus.getPlayer2());
    }

    public void sendUpdatePlayerGraveyard(GameStatus gameStatus, Player player) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_PLAYER_GRAVEYARD", player, player.getGraveyard().getCards())
        );
    }

    public void sendUpdateGraveyards(GameStatus gameStatus) {
        sendUpdatePlayerGraveyard(gameStatus, gameStatus.getPlayer1());
        sendUpdatePlayerGraveyard(gameStatus, gameStatus.getPlayer2());
    }

    public void sendUpdatePlayerLife(GameStatus gameStatus, Player player) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_PLAYER_LIFE", player, player.getLife())
        );
    }

    public void sendUpdateStack(GameStatus gameStatus) {
        eventSender.sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_STACK", gameStatus.getStack())
        );
    }
}

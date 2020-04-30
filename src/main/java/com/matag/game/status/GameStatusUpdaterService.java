package com.matag.game.status;

import com.matag.game.event.Event;
import com.matag.game.event.EventSender;
import com.matag.game.message.MessageEvent;
import com.matag.game.player.Player;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;

@Component
@AllArgsConstructor
public class GameStatusUpdaterService {
  private final EventSender eventSender;

  public void sendUpdateGameStatus(GameStatus gameStatus) {
    sendUpdateBattlefields(gameStatus);
    sendUpdateGraveyards(gameStatus);
    sendUpdateStack(gameStatus);
    sendUpdateLibrariesSize(gameStatus);
    sendUpdatePlayersLife(gameStatus);
    sendUpdateHands(gameStatus);
    sendUpdateTurn(gameStatus);
  }

  public void sendUpdateTurn(GameStatus gameStatus) {
    eventSender.sendToPlayers(
      asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
      new Event("UPDATE_TURN", gameStatus.getTurn())
    );
  }

  private void sendUpdateBattlefields(GameStatus gameStatus) {
    sendUpdatePlayerBattlefield(gameStatus, gameStatus.getPlayer1());
    sendUpdatePlayerBattlefield(gameStatus, gameStatus.getPlayer2());
  }

  private void sendUpdateGraveyards(GameStatus gameStatus) {
    sendUpdatePlayerGraveyard(gameStatus, gameStatus.getPlayer1());
    sendUpdatePlayerGraveyard(gameStatus, gameStatus.getPlayer2());
  }

  private void sendUpdateStack(GameStatus gameStatus) {
    eventSender.sendToPlayers(
      asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
      new Event("UPDATE_STACK", gameStatus.getStack().getItems())
    );
  }

  private void sendUpdateLibrariesSize(GameStatus gameStatus) {
    sendUpdatePlayerLibrarySize(gameStatus, gameStatus.getPlayer1());
    sendUpdatePlayerLibrarySize(gameStatus, gameStatus.getPlayer2());
  }

  private void sendUpdatePlayersLife(GameStatus gameStatus) {
    sendUpdatePlayerLife(gameStatus, gameStatus.getPlayer1());
    sendUpdatePlayerLife(gameStatus, gameStatus.getPlayer2());
  }

  private void sendUpdateHands(GameStatus gameStatus) {
    sendUpdatePlayerHand(gameStatus, gameStatus.getPlayer1());
    sendUpdatePlayerHand(gameStatus, gameStatus.getPlayer2());
  }

  public void sendMessage(String sessionId, MessageEvent messageEvent) {
    eventSender.sendToUser(sessionId, new Event("MESSAGE", messageEvent));
  }

  private void sendUpdatePlayerLibrarySize(GameStatus gameStatus, Player player) {
    eventSender.sendToPlayers(
      asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
      new Event("UPDATE_PLAYER_LIBRARY_SIZE", player, player.getLibrary().size())
    );
  }

  private void sendUpdatePlayerHand(GameStatus gameStatus, Player player) {
    Player otherPlayer = gameStatus.getOtherPlayer(player);
    eventSender.sendToPlayer(player, new Event("UPDATE_PLAYER_HAND", player, player.getHand().getCards()));
    eventSender.sendToPlayer(otherPlayer, new Event("UPDATE_PLAYER_HAND", player, player.getHand().maskedHand()));
  }

  private void sendUpdatePlayerBattlefield(GameStatus gameStatus, Player player) {
    eventSender.sendToPlayers(
      asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
      new Event("UPDATE_PLAYER_BATTLEFIELD", player, player.getBattlefield().getCards())
    );
  }

  private void sendUpdatePlayerGraveyard(GameStatus gameStatus, Player player) {
    eventSender.sendToPlayers(
      asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
      new Event("UPDATE_PLAYER_GRAVEYARD", player, player.getGraveyard().getCards())
    );
  }

  private void sendUpdatePlayerLife(GameStatus gameStatus, Player player) {
    eventSender.sendToPlayers(
      asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
      new Event("UPDATE_PLAYER_LIFE", player, player.getLife())
    );
  }
}

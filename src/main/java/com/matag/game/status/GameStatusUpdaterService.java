package com.matag.game.status;

import com.matag.game.event.Event;
import com.matag.game.event.EventSender;
import com.matag.game.message.MessageEvent;
import com.matag.game.player.Player;
import com.matag.game.player.PlayerUpdateEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.Arrays.asList;

@Component
@AllArgsConstructor
public class GameStatusUpdaterService {
  private final EventSender eventSender;

  public void sendUpdateGameStatus(GameStatus gameStatus) {
    sendUpdateHands(gameStatus);

    GameStatusUpdateEvent gameStatusUpdateEvent = GameStatusUpdateEvent.builder()
      .turn(gameStatus.getTurn())
      .stack(gameStatus.getStack().getItems())
      .playersUpdateEvents(Set.of(
        playerUpdateEvent(gameStatus.getPlayer1()),
        playerUpdateEvent(gameStatus.getPlayer2())
      ))
      .build();

    eventSender.sendToPlayers(
      asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
      new Event("UPDATE_GAME_STATUS", gameStatusUpdateEvent)
    );
  }

  private PlayerUpdateEvent playerUpdateEvent(Player player) {
    return PlayerUpdateEvent.builder()
      .name(player.getName())
      .life(player.getLife())
      .librarySize(player.getLibrary().size())
      .battlefield(player.getBattlefield().getCards())
      .graveyard(player.getGraveyard().getCards())
      .build();
  }

  private void sendUpdateHands(GameStatus gameStatus) {
    sendUpdatePlayerHand(gameStatus, gameStatus.getPlayer1());
    sendUpdatePlayerHand(gameStatus, gameStatus.getPlayer2());
  }

  public void sendMessage(String sessionId, MessageEvent messageEvent) {
    eventSender.sendToUser(sessionId, new Event("MESSAGE", messageEvent));
  }

  private void sendUpdatePlayerHand(GameStatus gameStatus, Player player) {
    Player otherPlayer = gameStatus.getOtherPlayer(player);
    eventSender.sendToPlayer(player, new Event("UPDATE_PLAYER_HAND", player, player.getHand().getCards()));
    eventSender.sendToPlayer(otherPlayer, new Event("UPDATE_PLAYER_HAND", player, player.getHand().maskedHand()));
  }
}

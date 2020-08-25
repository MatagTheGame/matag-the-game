package com.matag.game.status;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.event.Event;
import com.matag.game.event.EventSender;
import com.matag.game.message.MessageEvent;
import com.matag.game.player.Player;
import com.matag.game.player.PlayerUpdateEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static com.matag.game.turn.action._continue.NonStackActions.SCRY;
import static java.lang.Integer.parseInt;

@Component
@AllArgsConstructor
public class GameStatusUpdaterService {
  private final EventSender eventSender;
  private final CardInstanceFactory cardInstanceFactory;

  public void sendHealthcheck(String sessionId) {
    eventSender.sendToUser(sessionId, new Event("HEALTHCHECK"));
  }

  public void sendUpdateGameStatus(GameStatus gameStatus) {
    eventSender.sendToPlayer(
      gameStatus.getPlayer1(),
      new Event("UPDATE_GAME_STATUS", gameStatus.getPlayer1(), gameStatusUpdateEvent(gameStatus, gameStatus.getPlayer1()))
    );

    eventSender.sendToPlayer(
      gameStatus.getPlayer2(),
      new Event("UPDATE_GAME_STATUS", gameStatus.getPlayer2(), gameStatusUpdateEvent(gameStatus, gameStatus.getPlayer2()))
    );
  }

  private GameStatusUpdateEvent gameStatusUpdateEvent(GameStatus gameStatus, Player forPlayer) {
    return GameStatusUpdateEvent.builder()
        .turn(gameStatus.getTurn())
        .stack(gameStatus.getStack().getItems())
        .playersUpdateEvents(Set.of(
          playerUpdateEvent(gameStatus, gameStatus.getPlayer1(), forPlayer),
          playerUpdateEvent(gameStatus, gameStatus.getPlayer2(), forPlayer)
        ))
        .build();
  }

  private PlayerUpdateEvent playerUpdateEvent(GameStatus gameStatus, Player player, Player forPlayer) {
    return PlayerUpdateEvent.builder()
      .name(player.getName())
      .life(player.getLife())
      .librarySize(player.getLibrary().size())
      .visibleLibrary(playerVisibleLibraryToThem(gameStatus, player, forPlayer))
      .battlefield(player.getBattlefield().getCards())
      .graveyard(player.getGraveyard().getCards())
      .hand(playerHand(player, forPlayer))
      .build();
  }

  private List<CardInstance> playerVisibleLibraryToThem(GameStatus gameStatus, Player player, Player forPlayer) {
    if (SCRY.equals(gameStatus.getTurn().getTriggeredNonStackAction())) {
      var cardsToScry = parseInt(gameStatus.getTurn().getTriggeredNonStackActionParameter());
      var cardsScried = player.getLibrary().peek(cardsToScry);
      if (player == forPlayer) {
        return cardsScried;
      } else {
        return cardInstanceFactory.mask(cardsScried);
      }
    }
    return null;
  }

  private List<CardInstance> playerHand(Player player, Player forPlayer) {
    if (player == forPlayer) {
      return player.getHand().getCards();
    } else {
      return player.getHand().maskedHand();
    }
  }

  public void sendMessage(String sessionId, MessageEvent messageEvent) {
    eventSender.sendToUser(sessionId, new Event("MESSAGE", messageEvent));
  }
}

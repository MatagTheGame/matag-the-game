package com.matag.game.turn.action.player;

import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.finish.FinishGameService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DrawXCardsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DrawXCardsService.class);

  private final FinishGameService finishGameService;

  public void drawXCards(Player player, int cardsToDraw, GameStatus gameStatus) {
    if (cardsToDraw > 0) {
      var effectiveCardsToDraw = Math.min(cardsToDraw, player.getLibrary().size());
      for (var i = 0; i < effectiveCardsToDraw; i++) {
        var drawnCardInstance = player.getLibrary().draw();
        player.getHand().addCard(drawnCardInstance);
      }

      LOGGER.info("{} drew {} cards.", player.getName(), effectiveCardsToDraw);

      if (cardsToDraw > effectiveCardsToDraw) {
        var winner = gameStatus.getOtherPlayer(player);
        LOGGER.info("Player {} lose (needed to draw {} cards), Player {} win", player.getName(), cardsToDraw, winner.getName());
        finishGameService.setWinner(gameStatus, winner);
      }
    }
  }
}

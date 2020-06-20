package com.matag.game.turn.action.draw;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DrawXCardsService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DrawXCardsService.class);

  public void drawXCards(Player player, int cardsToDraw) {
    if (cardsToDraw > 0) {
      for (int i = 0; i < cardsToDraw; i++) {
        CardInstance drawnCardInstance = player.getLibrary().draw();
        player.getHand().addCard(drawnCardInstance);
      }

      LOGGER.info("{} drew {} cards.", player.getName(), cardsToDraw);
    }
  }
}

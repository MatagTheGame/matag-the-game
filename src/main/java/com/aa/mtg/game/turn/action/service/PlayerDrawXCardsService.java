package com.aa.mtg.game.turn.action.service;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PlayerDrawXCardsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDrawXCardsService.class);

    public void drawXCards(Player player, int cardsToDraw) {
        for (int i = 0; i < cardsToDraw; i++) {
            CardInstance drawnCardInstance = player.getLibrary().draw();
            player.getHand().addCard(drawnCardInstance);
        }

        LOGGER.info("{} drew {} cards.", player.getName(), cardsToDraw);
    }
}

package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DrawXCardsAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(DrawXCardsAction.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public DrawXCardsAction(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        int cardsToDraw = Integer.valueOf(parameter);

        Player currentPlayer = gameStatus.getCurrentPlayer();
        for (int i = 0; i < cardsToDraw; i++) {
            CardInstance drawnCardInstance = currentPlayer.getLibrary().draw();
            currentPlayer.getHand().addCard(drawnCardInstance);
        }

        gameStatusUpdaterService.sendUpdatePlayerHand(gameStatus, currentPlayer);
        gameStatusUpdaterService.sendUpdatePlayerLibrarySize(gameStatus, currentPlayer);
        LOGGER.info("{} drew {} cards.", currentPlayer.getName(), cardsToDraw);
    }
}

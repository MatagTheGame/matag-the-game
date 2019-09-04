package com.aa.mtg.game.turn.action.draw;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DrawXCardsAction implements AbilityAction {
    private final PlayerDrawXCardsService playerDrawXCardsService;

    @Autowired
    public DrawXCardsAction(PlayerDrawXCardsService playerDrawXCardsService) {
        this.playerDrawXCardsService = playerDrawXCardsService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        int cardsToDraw = Integer.valueOf(parameter);

        Player player;
        if (cardInstance == null) {
            player = gameStatus.getCurrentPlayer();
        } else {
            player = gameStatus.getPlayerByName(cardInstance.getController());
        }

        playerDrawXCardsService.drawXCards(player, cardsToDraw);
    }
}

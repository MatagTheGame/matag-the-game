package com.aa.mtg.game.turn.action.draw;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cardinstance.ability.AbilityAction;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DrawXCardsAction implements AbilityAction {
    private final DrawXCardsService drawXCardsService;

    @Autowired
    public DrawXCardsAction(DrawXCardsService drawXCardsService) {
        this.drawXCardsService = drawXCardsService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
        int cardsToDraw = Integer.valueOf(ability.getParameter(0));

        Player player;
        if (cardInstance == null) {
            player = gameStatus.getCurrentPlayer();
        } else {
            player = gameStatus.getPlayerByName(cardInstance.getController());
        }

        drawXCardsService.drawXCards(player, cardsToDraw);
    }
}

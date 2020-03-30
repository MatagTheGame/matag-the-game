package com.matag.game.turn.action.draw;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.ability.AbilityAction;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DrawXCardsAction implements AbilityAction {
  private final DrawXCardsService drawXCardsService;

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

package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.player.DrawXCardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DrawPhase implements Phase {
  public static final String DR = "DR";

  private final DrawXCardsService drawXCardsService;

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getTurnNumber() > 1) {
      drawXCardsService.drawXCards(gameStatus.getCurrentPlayer(), 1);
    }
    gameStatus.getTurn().setCurrentPhase(Main1Phase.M1);
  }
}

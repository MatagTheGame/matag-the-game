package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.draw.DrawXCardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@Component
public class DrawPhase implements Phase {
  public static final String DR = "DR";

  private final DrawXCardsService drawXCardsService;

  @Autowired
  public DrawPhase(DrawXCardsService drawXCardsService) {
    this.drawXCardsService = drawXCardsService;
  }

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getTurnNumber() > 1) {
      drawXCardsService.drawXCards(gameStatus.getCurrentPlayer(), 1);
    }
    gameStatus.getTurn().setCurrentPhase(M1);
  }
}

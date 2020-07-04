package com.matag.game.turn.phases.beginning;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.player.DrawXCardsService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main1.Main1Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DrawPhase extends AbstractPhase {
  public static final String DR = "DR";

  private final DrawXCardsService drawXCardsService;
  private final Main1Phase main1Phase;

  @Override
  public String getName() {
    return DR;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return main1Phase;
  }

  @Override
  public void action(GameStatus gameStatus) {
    super.action(gameStatus);

    if (gameStatus.getTurn().getTurnNumber() > 1) {
      drawXCardsService.drawXCards(gameStatus.getCurrentPlayer(), 1);
    }
    gameStatus.getTurn().setCurrentPhase(Main1Phase.M1);
  }
}

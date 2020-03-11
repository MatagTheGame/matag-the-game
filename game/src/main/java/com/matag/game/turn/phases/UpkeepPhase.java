package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.DrawPhase.DR;

@Component
public class UpkeepPhase implements Phase {
  public static final String UP = "UP";

  private final DrawPhase drawPhase;

  public UpkeepPhase(DrawPhase drawPhase) {
    this.drawPhase = drawPhase;
  }


  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().passPriority(gameStatus);

    } else {
      gameStatus.getTurn().setCurrentPhase(DR);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      drawPhase.apply(gameStatus);
    }
  }
}

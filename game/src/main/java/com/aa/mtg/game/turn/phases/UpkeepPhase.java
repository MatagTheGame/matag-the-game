package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.DrawPhase.DR;

@Component
public class UpkeepPhase implements Phase {
  public static final String UP = "UP";

  private final DrawPhase drawPhase;

  @Autowired
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

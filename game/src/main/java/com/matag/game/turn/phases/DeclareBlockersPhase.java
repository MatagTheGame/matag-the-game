package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class DeclareBlockersPhase implements Phase {
  public static final String DB = "DB";

  @Override
  public void apply(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    gameStatus.getTurn().setCurrentPhase(AfterDeclareBlockersPhase.AB);
  }
}

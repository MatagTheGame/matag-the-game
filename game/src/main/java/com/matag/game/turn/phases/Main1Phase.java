package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.BeginCombatPhase.BC;

@Component
public class Main1Phase implements Phase {
  public static final String M1 = "M1";

  private final BeginCombatPhase beginCombatPhase;

  public Main1Phase(BeginCombatPhase beginCombatPhase) {
    this.beginCombatPhase = beginCombatPhase;
  }

  @Override
  public void apply(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhase(BC);
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    beginCombatPhase.apply(gameStatus);
  }
}

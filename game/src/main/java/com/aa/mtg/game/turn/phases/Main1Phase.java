package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;

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

package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.BeginCombatPhase.BC;

@Component
@AllArgsConstructor
public class Main1Phase implements Phase {
  public static final String M1 = "M1";

  private final BeginCombatPhase beginCombatPhase;

  @Override
  public void apply(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhase(BC);
    beginCombatPhase.apply(gameStatus);
  }
}

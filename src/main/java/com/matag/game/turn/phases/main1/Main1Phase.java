package com.matag.game.turn.phases.main1;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.combat.BeginCombatPhase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.combat.BeginCombatPhase.BC;

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

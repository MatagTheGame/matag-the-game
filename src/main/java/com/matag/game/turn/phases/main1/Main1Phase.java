package com.matag.game.turn.phases.main1;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.combat.BeginCombatPhase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Main1Phase extends AbstractPhase {
  public static final String M1 = "M1";

  private final BeginCombatPhase beginCombatPhase;

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return beginCombatPhase;
  }

  @Override
  public String getName() {
    return M1;
  }
}

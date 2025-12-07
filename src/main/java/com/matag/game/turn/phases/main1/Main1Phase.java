package com.matag.game.turn.phases.main1;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.combat.BeginCombatPhase;
import org.springframework.stereotype.Component;

@Component
public class Main1Phase extends AbstractPhase {
  public static final String M1 = "M1";

  private final BeginCombatPhase beginCombatPhase;

    public Main1Phase(AutocontinueChecker autocontinueChecker, BeginCombatPhase beginCombatPhase) {
        super(autocontinueChecker);
        this.beginCombatPhase = beginCombatPhase;
    }

    @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return beginCombatPhase;
  }

  @Override
  public String getName() {
    return M1;
  }
}

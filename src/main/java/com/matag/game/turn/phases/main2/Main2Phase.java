package com.matag.game.turn.phases.main2;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.ending.EndTurnPhase;
import org.springframework.stereotype.Component;

@Component
public class Main2Phase extends AbstractPhase {
  public static final String M2 = "M2";

  private final EndTurnPhase endTurnPhase;

    public Main2Phase(AutocontinueChecker autocontinueChecker, EndTurnPhase endTurnPhase) {
        super(autocontinueChecker);
        this.endTurnPhase = endTurnPhase;
    }

    @Override
  public String getName() {
    return M2;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return endTurnPhase;
  }
}

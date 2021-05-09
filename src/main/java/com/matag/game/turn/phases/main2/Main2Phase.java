package com.matag.game.turn.phases.main2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.ending.EndTurnPhase;

@Component
public class Main2Phase extends AbstractPhase {
  public static final String M2 = "M2";

  @Autowired
  private EndTurnPhase endTurnPhase;

  @Override
  public String getName() {
    return M2;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return endTurnPhase;
  }
}

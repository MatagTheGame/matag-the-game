package com.matag.game.turn.phases.ending;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;

@Component
public class EndTurnPhase extends AbstractPhase {
  public static final String ET = "ET";

  @Autowired
  private CleanupPhase cleanupPhase;

  @Override
  public String getName() {
    return ET;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return cleanupPhase;
  }
}

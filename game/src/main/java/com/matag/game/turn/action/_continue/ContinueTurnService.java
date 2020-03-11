package com.matag.game.turn.action._continue;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.PhaseFactory;
import org.springframework.stereotype.Component;

@Component
public class ContinueTurnService {
  private final PhaseFactory phaseFactory;

  public ContinueTurnService(PhaseFactory phaseFactory) {
    this.phaseFactory = phaseFactory;
  }

  public void continueTurn(GameStatus gameStatus) {
    Phase phase = phaseFactory.get(gameStatus.getTurn().getCurrentPhase());
    phase.apply(gameStatus);
  }
}

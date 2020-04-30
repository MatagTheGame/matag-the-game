package com.matag.game.turn.action._continue;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.PhaseFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContinueTurnService {
  private final PhaseFactory phaseFactory;

  public void continueTurn(GameStatus gameStatus) {
    Phase phase = phaseFactory.get(gameStatus.getTurn().getCurrentPhase());
    phase.apply(gameStatus);
  }
}

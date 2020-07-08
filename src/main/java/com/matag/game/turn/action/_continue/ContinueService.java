package com.matag.game.turn.action._continue;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.PhaseFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContinueService {
  private final PhaseFactory phaseFactory;

  public void next(GameStatus gameStatus) {
    var phase = phaseFactory.get(gameStatus.getTurn().getCurrentPhase());
    phase.next(gameStatus);
  }
}

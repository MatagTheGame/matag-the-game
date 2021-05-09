package com.matag.game.turn.action._continue;

import org.springframework.stereotype.Component;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.PhaseFactory;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ContinueService {
  private final PhaseFactory phaseFactory;

  public void set(GameStatus gameStatus) {
    var phase = phaseFactory.get(gameStatus.getTurn().getCurrentPhase());
    phase.set(gameStatus);
  }

  public void next(GameStatus gameStatus) {
    var phase = phaseFactory.get(gameStatus.getTurn().getCurrentPhase());
    phase.next(gameStatus);
  }
}

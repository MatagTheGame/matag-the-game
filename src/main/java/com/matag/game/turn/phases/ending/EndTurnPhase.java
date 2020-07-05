package com.matag.game.turn.phases.ending;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EndTurnPhase extends AbstractPhase {
  public static final String ET = "ET";

  private final CleanupPhase cleanupPhase;

  @Override
  public String getName() {
    return ET;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return cleanupPhase;
  }
}

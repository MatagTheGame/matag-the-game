package com.matag.game.turn.phases.beginning;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpkeepPhase extends AbstractPhase {
  public static final String UP = "UP";

  private final DrawPhase drawPhase;

  @Override
  public String getName() {
    return UP;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return drawPhase;
  }
}

package com.matag.game.turn.phases.beginning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;

@Component
public class UpkeepPhase extends AbstractPhase {
  public static final String UP = "UP";

  @Autowired
  private DrawPhase drawPhase;

  @Override
  public String getName() {
    return UP;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return drawPhase;
  }
}

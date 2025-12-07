package com.matag.game.turn.phases.beginning;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.stereotype.Component;

@Component
public class UpkeepPhase extends AbstractPhase {
  public static final String UP = "UP";

  private final DrawPhase drawPhase;

    public UpkeepPhase(AutocontinueChecker autocontinueChecker, DrawPhase drawPhase) {
        super(autocontinueChecker);
        this.drawPhase = drawPhase;
    }

    @Override
  public String getName() {
    return UP;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return drawPhase;
  }
}

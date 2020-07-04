package com.matag.game.turn.phases.beginning;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.beginning.DrawPhase.DR;

@Component
@AllArgsConstructor
public class UpkeepPhase extends AbstractPhase {
  public static final String UP = "UP";

  private final DrawPhase drawPhase;
  private final AutocontinueChecker autocontinueChecker;

  @Override
  public String getName() {
    return UP;
  }

  @Override
  public void next(GameStatus gameStatus) {
    super.next(gameStatus);
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().passPriority(gameStatus);

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        next(gameStatus);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(DR);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      drawPhase.next(gameStatus);
    }
  }
}

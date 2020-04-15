package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.DrawPhase.DR;

@Component
@AllArgsConstructor
public class UpkeepPhase implements Phase {
  public static final String UP = "UP";

  private final DrawPhase drawPhase;
  private final AutocontinueChecker autocontinueChecker;

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().passPriority(gameStatus);

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        apply(gameStatus);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(DR);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      drawPhase.apply(gameStatus);
    }
  }
}

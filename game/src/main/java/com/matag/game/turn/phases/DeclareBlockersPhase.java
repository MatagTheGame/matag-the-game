package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeclareBlockersPhase implements Phase {
  public static final String DB = "DB";

  private AutocontinueChecker autocontinueChecker;
  private AfterDeclareBlockersPhase afterDeclareBlockersPhase;

  @Override
  public void apply(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    gameStatus.getTurn().setCurrentPhase(AfterDeclareBlockersPhase.AB);

    if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
      afterDeclareBlockersPhase.apply(gameStatus);
    }
  }
}

package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeclareBlockersPhase extends AbstractPhase {
  public static final String DB = "DB";

  private final AutocontinueChecker autocontinueChecker;

  @Autowired
  private AfterDeclareBlockersPhase afterDeclareBlockersPhase;

  public DeclareBlockersPhase(AutocontinueChecker autocontinueChecker) {
    this.autocontinueChecker = autocontinueChecker;
  }

  @Override
  public String getName() {
    return DB;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return afterDeclareBlockersPhase;
  }

  @Override
  public void next(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    gameStatus.getTurn().setCurrentPhase(AfterDeclareBlockersPhase.AB);

    if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
      afterDeclareBlockersPhase.next(gameStatus);
    }
  }
}

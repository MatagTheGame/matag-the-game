package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.combat.FirstStrikePhase.FS;

@Component
public class AfterDeclareBlockersPhase extends AbstractPhase {
  public static final String AB = "AB";

  private final AutocontinueChecker autocontinueChecker;

  @Autowired
  private FirstStrikePhase firstStrikePhase;

  public AfterDeclareBlockersPhase(AutocontinueChecker autocontinueChecker) {
    this.autocontinueChecker = autocontinueChecker;
  }

  @Override
  public String getName() {
    return AB;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return firstStrikePhase;
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        next(gameStatus);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(FS);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      firstStrikePhase.next(gameStatus);
    }
  }
}

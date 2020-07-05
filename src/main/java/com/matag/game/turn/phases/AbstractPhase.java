package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.matag.game.turn.phases.PhaseUtils.isMainPhase;
import static com.matag.game.turn.phases.PhaseUtils.isPriorityAllowed;
import static com.matag.game.turn.phases.ending.CleanupPhase.CL;

public abstract class AbstractPhase implements Phase {
  @Autowired
  private AutocontinueChecker autocontinueChecker;

  @Override
  public void action(GameStatus gameStatus) {
    gameStatus.getTurn().setPhaseActioned(true);
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.getTurn().isPhaseActioned()) {
      evaluateNext(gameStatus);

    } else {
      action(gameStatus);
      if (!isMainPhase(getName()) && gameStatus.getTurn().getTriggeredNonStackAction() == null) {
        evaluateNext(gameStatus);
      }
    }
  }

  private void evaluateNext(GameStatus gameStatus) {
    if (List.of("UT", "UP", "DR", "M1", "M2", "ET", "CL").contains(getName())) {
      if (isPriorityAllowed(getName())) {
        if (isCurrentPlayerActive(gameStatus)) {
          gameStatus.getTurn().passPriority(gameStatus);

          if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
            next(gameStatus);
          }

        } else {
          moveToNextPhase(gameStatus);
        }

      } else {
        moveToNextPhase(gameStatus);
      }
    }
  }

  private boolean isCurrentPlayerActive(GameStatus gameStatus) {
    return gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName());
  }

  private void moveToNextPhase(GameStatus gameStatus) {
    if (getName().equals(CL)) {
      gameStatus.getTurn().setCurrentTurnPlayer(gameStatus.getNonCurrentPlayer().getName());
      gameStatus.getTurn().increaseTurnNumber();
    }

    Phase nextPhase = getNextPhase(gameStatus);
    gameStatus.getTurn().setCurrentPhase(nextPhase.getName());
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getTurn().getCurrentTurnPlayer());
    gameStatus.getTurn().setPhaseActioned(false);
    nextPhase.next(gameStatus);
  }
}

package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.combat.DeclareAttackersPhase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    if (gameStatus.getTurn().isEnded()) {
      return;
    }

    if (!gameStatus.getTurn().isPhaseActioned()) {
      action(gameStatus);
      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        moveNext(gameStatus);
      }

    } else {
      moveNext(gameStatus);
    }
  }

  private void moveNext(GameStatus gameStatus) {
    // TODO delete this when abstraction is complete
    if (List.of("DA", "DB", "AB").contains(getName())) {
      return;
    }

    if (isPriorityAllowed(getName())) {
      if (gameStatus.isCurrentPlayerActive()) {
        moveToNextPlayer(gameStatus);

      } else {
        moveToNextPhase(gameStatus);
      }

    } else {
      moveToNextPhase(gameStatus);
    }
  }

  private void moveToNextPlayer(GameStatus gameStatus) {
    gameStatus.getTurn().passPriority(gameStatus);

    if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
      moveToNextPhase(gameStatus);
    }
  }

  private void moveToNextPhase(GameStatus gameStatus) {
    if (getName().equals(CL)) {
      gameStatus.getTurn().setCurrentTurnPlayer(gameStatus.getNonCurrentPlayer().getName());
      gameStatus.getTurn().increaseTurnNumber();
    }

    var nextPhase = getNextPhase(gameStatus);
    gameStatus.getTurn().setCurrentPhase(nextPhase.getName());
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getTurn().getCurrentTurnPlayer());
    gameStatus.getTurn().setPhaseActioned(false);

    // TODO delete this temporary hack while doing abstraction
    if (!(nextPhase instanceof DeclareAttackersPhase)){
      nextPhase.next(gameStatus);
    }
  }
}

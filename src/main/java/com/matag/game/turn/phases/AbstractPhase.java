package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import lombok.RequiredArgsConstructor;

import static com.matag.game.turn.phases.PhaseUtils.isPriorityAllowed;
import static com.matag.game.turn.phases.ending.CleanupPhase.CL;

@RequiredArgsConstructor
public abstract class AbstractPhase implements Phase {
  private final AutocontinueChecker autocontinueChecker;

  @Override
  public void action(GameStatus gameStatus) {}

  @Override
  public void set(GameStatus gameStatus) {
    if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
      next(gameStatus);
    }
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.getTurn().isEnded()) {
      return;
    }

    moveNext(gameStatus);
  }

  private void moveNext(GameStatus gameStatus) {
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
    Phase nextPhase;

    if (getName().equals(CL)) {
      gameStatus.getTurn().setCurrentTurnPlayer(gameStatus.getNonCurrentPlayer().getName());
      gameStatus.getTurn().increaseTurnNumber();
      throw new RuntimeException("What can I do?");

    } else {
      nextPhase = getNextPhase(gameStatus);
    }

    gameStatus.getTurn().setCurrentPhase(nextPhase.getName());
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getTurn().getCurrentTurnPlayer());
    nextPhase.action(gameStatus);

    if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
      nextPhase.next(gameStatus);
    }
  }
}

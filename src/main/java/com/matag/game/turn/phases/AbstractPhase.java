package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;

import java.util.List;

public abstract class AbstractPhase implements Phase {
  @Override
  public void action(GameStatus gameStatus) {

  }

  @Override
  public void next(GameStatus gameStatus) {
    action(gameStatus);


    if (List.of("UT", "UP", "DR").contains(getName())) {
      if (PhaseUtils.isPriorityAllowed(getName())) {


      } else {
        Phase nextPhase = getNextPhase(gameStatus);
        gameStatus.getTurn().setCurrentPhase(nextPhase.getName());
        nextPhase.next(gameStatus);
      }
    }
  }
}

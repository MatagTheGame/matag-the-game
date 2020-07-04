package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;

public abstract class AbstractPhase implements Phase {
  protected boolean actioned;

  @Override
  public void action(GameStatus gameStatus) {
    actioned = true;
  }

  @Override
  public void next(GameStatus gameStatus) {
    action(gameStatus);
  }
}

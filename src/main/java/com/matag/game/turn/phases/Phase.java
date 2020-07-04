package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;

public interface Phase {

  void next(GameStatus gameStatus);

}

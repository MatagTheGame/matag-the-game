package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;

public interface Phase {

  String getName();

  void action(GameStatus gameStatus);

  void next(GameStatus gameStatus);

}

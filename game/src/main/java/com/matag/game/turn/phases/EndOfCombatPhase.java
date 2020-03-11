package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class EndOfCombatPhase implements Phase {
  public static final String EC = "EC";

  @Override
  public void apply(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

    gameStatus.getCurrentPlayer().getBattlefield().removeAttacking();
    gameStatus.getNonCurrentPlayer().getBattlefield().removeBlocking();
  }
}

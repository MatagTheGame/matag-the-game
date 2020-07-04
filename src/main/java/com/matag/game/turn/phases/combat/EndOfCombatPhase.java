package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.main2.Main2Phase;
import com.matag.game.turn.phases.Phase;
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

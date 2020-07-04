package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.stereotype.Component;

@Component
public class EndOfCombatPhase extends AbstractPhase {
  public static final String EC = "EC";

  @Override
  public String getName() {
    return EC;
  }

  @Override
  public void next(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

    gameStatus.getCurrentPlayer().getBattlefield().removeAttacking();
    gameStatus.getNonCurrentPlayer().getBattlefield().removeBlocking();
  }
}

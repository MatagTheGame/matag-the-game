package com.matag.game.turn.phases.combat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;

@Component
public class EndOfCombatPhase extends AbstractPhase {
  public static final String EC = "EC";

  @Autowired
  private Main2Phase main2Phase;

  @Override
  public String getName() {
    return EC;
  }

  @Override
  public void action(GameStatus gameStatus) {
    super.action(gameStatus);
    gameStatus.getCurrentPlayer().getBattlefield().removeAttacking();
    gameStatus.getNonCurrentPlayer().getBattlefield().removeBlocking();
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return main2Phase;
  }
}

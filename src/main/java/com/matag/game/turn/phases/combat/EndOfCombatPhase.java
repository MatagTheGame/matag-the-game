package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
  public Phase getNextPhase(GameStatus gameStatus) {
    return main2Phase;
  }

  @Override
  public void next(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

    gameStatus.getCurrentPlayer().getBattlefield().removeAttacking();
    gameStatus.getNonCurrentPlayer().getBattlefield().removeBlocking();
    gameStatus.getTurn().setPhaseActioned(false);
    main2Phase.next(gameStatus);
  }
}

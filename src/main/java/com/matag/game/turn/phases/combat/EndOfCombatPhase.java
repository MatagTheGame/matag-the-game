package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.stereotype.Component;

@Component
public class EndOfCombatPhase extends AbstractPhase {
  public static final String EC = "EC";

  private final Main2Phase main2Phase;

    public EndOfCombatPhase(AutocontinueChecker autocontinueChecker, Main2Phase main2Phase) {
        super(autocontinueChecker);
        this.main2Phase = main2Phase;
    }

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

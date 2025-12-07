package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.stereotype.Component;

@Component
public class BeginCombatPhase extends AbstractPhase {
  public static final String BC = "BC";

  private final DeclareAttackersPhase declareAttackersPhase;
  private final Main2Phase main2Phase;

    public BeginCombatPhase(AutocontinueChecker autocontinueChecker, DeclareAttackersPhase declareAttackersPhase, Main2Phase main2Phase) {
        super(autocontinueChecker);
        this.declareAttackersPhase = declareAttackersPhase;
        this.main2Phase = main2Phase;
    }

    @Override
  public String getName() {
    return BC;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    if (gameStatus.getCurrentPlayer().getBattlefield().search().canAnyCreatureAttack()) {
      return declareAttackersPhase;
    } else {
      return main2Phase;
    }
  }
}

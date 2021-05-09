package com.matag.game.turn.phases.combat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;

@Component
public class BeginCombatPhase extends AbstractPhase {
  public static final String BC = "BC";

  @Autowired
  private DeclareAttackersPhase declareAttackersPhase;

  @Autowired
  private Main2Phase main2Phase;

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

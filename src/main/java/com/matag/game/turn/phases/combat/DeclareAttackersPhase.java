package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.action._continue.NonStackActions.DECLARE_ATTACKERS;

@Component
public class DeclareAttackersPhase extends AbstractPhase {
  public static final String DA = "DA";

  @Autowired
  private DeclareBlockersPhase declareBlockersPhase;

  @Autowired
  private Main2Phase main2Phase;

  @Override
  public String getName() {
    return DA;
  }

  @Override
  public void action(GameStatus gameStatus) {
    gameStatus.getTurn().setTriggeredNonStackAction(DECLARE_ATTACKERS);
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    if (gameStatus.getCurrentPlayer().getBattlefield().getAttackingCreatures().isEmpty()) {
      return main2Phase;
    } else {
      return declareBlockersPhase;
    }
  }
}

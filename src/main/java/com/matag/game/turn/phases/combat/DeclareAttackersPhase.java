package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.action._continue.InputRequiredActions.DECLARE_ATTACKERS;

@Component
public class DeclareAttackersPhase extends AbstractPhase {
  public static final String DA = "DA";

  private final DeclareBlockersPhase declareBlockersPhase;
  private final Main2Phase main2Phase;

    public DeclareAttackersPhase(AutocontinueChecker autocontinueChecker, DeclareBlockersPhase declareBlockersPhase, Main2Phase main2Phase) {
        super(autocontinueChecker);
        this.declareBlockersPhase = declareBlockersPhase;
        this.main2Phase = main2Phase;
    }

    @Override
  public String getName() {
    return DA;
  }

  @Override
  public void action(GameStatus gameStatus) {
    gameStatus.getTurn().setInputRequiredAction(DECLARE_ATTACKERS);
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

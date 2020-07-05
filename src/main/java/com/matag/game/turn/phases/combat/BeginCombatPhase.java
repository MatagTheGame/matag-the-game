package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;

@Component
public class BeginCombatPhase extends AbstractPhase {
  public static final String BC = "BC";

  private final AutocontinueChecker autocontinueChecker;

  public BeginCombatPhase(AutocontinueChecker autocontinueChecker) {
    this.autocontinueChecker = autocontinueChecker;
  }

  @Autowired
  private Main2Phase main2Phase;

  @Override
  public String getName() {
    return BC;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    // TODO implement
    return null;
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      if (gameStatus.getCurrentPlayer().getBattlefield().search().canAnyCreatureAttack()) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

        if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
          next(gameStatus);
        }

      } else {
        gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
        gameStatus.getTurn().setPhaseActioned(false);
        main2Phase.next(gameStatus);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(DA);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    }
  }
}

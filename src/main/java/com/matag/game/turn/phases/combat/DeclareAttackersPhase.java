package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeclareAttackersPhase extends AbstractPhase {
  public static final String DA = "DA";

  private final AutocontinueChecker autocontinueChecker;

  @Autowired
  private AfterDeclareBlockersPhase afterDeclareBlockersPhase;

  @Autowired
  private Main2Phase main2Phase;

  public DeclareAttackersPhase(AutocontinueChecker autocontinueChecker) {
    this.autocontinueChecker = autocontinueChecker;
  }

  @Override
  public String getName() {
    return DA;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return afterDeclareBlockersPhase;
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.isCurrentPlayerActive()) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        next(gameStatus);
      }

    } else {
      if (!gameStatus.getCurrentPlayer().getBattlefield().getAttackingCreatures().isEmpty()) {
        if (gameStatus.getNonCurrentPlayer().getBattlefield().search().canAnyCreatureBlock()) {
          gameStatus.getTurn().setCurrentPhase(DeclareBlockersPhase.DB);
          gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

        } else {
          gameStatus.getTurn().setCurrentPhase(AfterDeclareBlockersPhase.AB);
          gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

          if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
            afterDeclareBlockersPhase.next(gameStatus);
          }
        }
      } else {
        gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
        gameStatus.getTurn().setPhaseActioned(false);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        main2Phase.next(gameStatus);
      }
    }
  }
}

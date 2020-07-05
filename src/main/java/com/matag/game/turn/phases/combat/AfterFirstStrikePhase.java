package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AfterFirstStrikePhase extends AbstractPhase {
  public static final String AF = "AF";

  private final AutocontinueChecker autocontinueChecker;

  @Autowired
  private CombatDamagePhase combatDamagePhase;

  public AfterFirstStrikePhase(AutocontinueChecker autocontinueChecker) {
    this.autocontinueChecker = autocontinueChecker;
  }

  @Override
  public String getName() {
    return AF;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return combatDamagePhase;
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        next(gameStatus);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(CombatDamagePhase.CD);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      combatDamagePhase.next(gameStatus);
    }
  }
}

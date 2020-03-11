package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class FirstStrikePhase implements Phase {
  public static final String FS = "FS";

  private final CombatDamagePhase combatDamagePhase;

  public FirstStrikePhase(CombatDamagePhase combatDamagePhase) {
    this.combatDamagePhase = combatDamagePhase;
  }

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      // TODO Antonio: check if any creature has first strike
      apply(gameStatus);

    } else {
      gameStatus.getTurn().setCurrentPhase(CombatDamagePhase.CD);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      combatDamagePhase.apply(gameStatus);
    }
  }
}

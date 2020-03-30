package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FirstStrikePhase implements Phase {
  public static final String FS = "FS";

  private final CombatDamagePhase combatDamagePhase;

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

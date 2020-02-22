package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.CombatDamagePhase.CD;

@Component
public class FirstStrikePhase implements Phase {
  public static final String FS = "FS";

  private final CombatDamagePhase combatDamagePhase;

  @Autowired
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
      gameStatus.getTurn().setCurrentPhase(CD);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      combatDamagePhase.apply(gameStatus);
    }
  }
}

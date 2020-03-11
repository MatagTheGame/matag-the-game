package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.combat.CombatService;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.EndOfCombatPhase.EC;

@Component
public class CombatDamagePhase implements Phase {
  public static final String CD = "CD";

  private final CombatService combatService;
  private final EndOfCombatPhase endOfCombatPhase;

  public CombatDamagePhase(CombatService combatService, EndOfCombatPhase endOfCombatPhase) {
    this.combatService = combatService;
    this.endOfCombatPhase = endOfCombatPhase;
  }

  @Override
  public void apply(GameStatus gameStatus) {
    combatService.dealCombatDamage(gameStatus);

    if (!gameStatus.getTurn().isEnded()) {
      gameStatus.getTurn().setCurrentPhase(EC);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      endOfCombatPhase.apply(gameStatus);
    }
  }
}

package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.combat.CombatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.EndOfCombatPhase.EC;

@Component
public class CombatDamagePhase implements Phase {
  public static final String CD = "CD";

  private final CombatService combatService;
  private final EndOfCombatPhase endOfCombatPhase;

  @Autowired
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

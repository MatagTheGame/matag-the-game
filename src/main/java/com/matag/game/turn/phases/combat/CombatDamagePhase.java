package com.matag.game.turn.phases.combat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;

@Component
public class CombatDamagePhase extends AbstractPhase {
  public static final String CD = "CD";

  private final CombatService combatService;

  @Autowired
  private EndOfCombatPhase endOfCombatPhase;

  public CombatDamagePhase(CombatService combatService) {
    this.combatService = combatService;
  }

  @Override
  public String getName() {
    return CD;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return endOfCombatPhase;
  }

  @Override
  public void action(GameStatus gameStatus) {
    super.action(gameStatus);
    combatService.dealCombatDamage(gameStatus);
  }
}

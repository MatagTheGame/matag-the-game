package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.stereotype.Component;

@Component
public class CombatDamagePhase extends AbstractPhase {
  public static final String CD = "CD";

  private final CombatService combatService;
  private final EndOfCombatPhase endOfCombatPhase;

  public CombatDamagePhase(AutocontinueChecker autocontinueChecker, CombatService combatService, EndOfCombatPhase endOfCombatPhase) {
      super(autocontinueChecker);
      this.combatService = combatService;
    this.endOfCombatPhase = endOfCombatPhase;
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

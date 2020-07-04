package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.phases.Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.combat.EndOfCombatPhase.EC;

@Component
@AllArgsConstructor
public class CombatDamagePhase implements Phase {
  public static final String CD = "CD";

  private final CombatService combatService;
  private final EndOfCombatPhase endOfCombatPhase;

  @Override
  public void next(GameStatus gameStatus) {
    combatService.dealCombatDamage(gameStatus);

    if (!gameStatus.getTurn().isEnded()) {
      gameStatus.getTurn().setCurrentPhase(EC);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      endOfCombatPhase.next(gameStatus);
    }
  }
}

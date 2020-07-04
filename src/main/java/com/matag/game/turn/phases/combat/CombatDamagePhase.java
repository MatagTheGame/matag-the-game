package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.combat.EndOfCombatPhase.EC;

@Component
@AllArgsConstructor
public class CombatDamagePhase extends AbstractPhase {
  public static final String CD = "CD";

  private final CombatService combatService;
  private final EndOfCombatPhase endOfCombatPhase;

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

  @Override
  public void next(GameStatus gameStatus) {
    super.next(gameStatus);

    if (!gameStatus.getTurn().isEnded()) {
      gameStatus.getTurn().setCurrentPhase(EC);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      endOfCombatPhase.next(gameStatus);
    }
  }
}

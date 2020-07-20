package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.type.AbilityType.DOUBLE_STRIKE;
import static com.matag.cards.ability.type.AbilityType.FIRST_STRIKE;
import static com.matag.game.turn.action._continue.NonStackActions.DECLARE_BLOCKERS;

@Component
public class DeclareBlockersPhase extends AbstractPhase {
  public static final String DB = "DB";

  @Autowired
  private FirstStrikePhase firstStrikePhase;

  @Autowired
  private CombatDamagePhase combatDamagePhase;

  @Override
  public String getName() {
    return DB;
  }

  @Override
  public void action(GameStatus gameStatus) {
    if (gameStatus.getNonCurrentPlayer().getBattlefield().search().canAnyCreatureBlock()) {
      gameStatus.getTurn().setTriggeredNonStackAction(DECLARE_BLOCKERS);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());
    }
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    var hasFirstStrike = gameStatus.getCurrentPlayer().getBattlefield().search()
      .withAnyFixedAbility(List.of(FIRST_STRIKE, DOUBLE_STRIKE))
      .isNotEmpty();

    if (hasFirstStrike) {
      return firstStrikePhase;
    } else {
      return combatDamagePhase;
    }
  }
}

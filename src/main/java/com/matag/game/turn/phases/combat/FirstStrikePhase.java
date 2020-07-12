package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.ConsolidateStatusService;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.type.AbilityType.DOUBLE_STRIKE;
import static com.matag.cards.ability.type.AbilityType.FIRST_STRIKE;

@Component
public class FirstStrikePhase extends AbstractPhase {
  public static final String FS = "FS";

  private final CombatService combatService;
  private final ConsolidateStatusService consolidateStatusService;

  @Autowired
  private CombatDamagePhase combatDamagePhase;

  public FirstStrikePhase(CombatService combatService, ConsolidateStatusService consolidateStatusService) {
    this.combatService = combatService;
    this.consolidateStatusService = consolidateStatusService;
  }

  @Override
  public String getName() {
    return FS;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return combatDamagePhase;
  }

  @Override
  public void action(GameStatus gameStatus) {
    super.action(gameStatus);
    var executePhase = gameStatus.getCurrentPlayer().getBattlefield().search()
      .withAnyFixedAbility(List.of(FIRST_STRIKE, DOUBLE_STRIKE))
      .isNotEmpty();

    if (executePhase) {
      combatService.dealCombatDamage(gameStatus);
      consolidateStatusService.consolidate(gameStatus);
    }
  }
}

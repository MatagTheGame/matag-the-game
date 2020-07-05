package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.action._continue.ConsolidateStatusService;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.type.AbilityType.DOUBLE_STRIKE;
import static com.matag.cards.ability.type.AbilityType.FIRST_STRIKE;
import static com.matag.game.turn.phases.combat.AfterFirstStrikePhase.AF;

@Component
public class FirstStrikePhase extends AbstractPhase {
  public static final String FS = "FS";

  private final AutocontinueChecker autocontinueChecker;
  private final CombatService combatService;
  private final ConsolidateStatusService consolidateStatusService;

  @Autowired
  private AfterFirstStrikePhase afterFirstStrikePhase;

  @Autowired
  private CombatDamagePhase combatDamagePhase;

  public FirstStrikePhase(AutocontinueChecker autocontinueChecker, CombatService combatService, ConsolidateStatusService consolidateStatusService) {
    this.autocontinueChecker = autocontinueChecker;
    this.combatService = combatService;
    this.consolidateStatusService = consolidateStatusService;
  }

  @Override
  public String getName() {
    return FS;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return afterFirstStrikePhase;
  }

  @Override
  public void next(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

    boolean executePhase = gameStatus.getCurrentPlayer().getBattlefield().search()
      .withAnyFixedAbility(List.of(FIRST_STRIKE, DOUBLE_STRIKE))
      .isNotEmpty();

    if (executePhase) {
      combatService.dealCombatDamage(gameStatus);
      consolidateStatusService.consolidate(gameStatus);

      if (!gameStatus.getTurn().isEnded()) {
        gameStatus.getTurn().setCurrentPhase(AF);
        if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
          afterFirstStrikePhase.next(gameStatus);
        }
      }

    } else {
      if (!gameStatus.getTurn().isEnded()) {
        gameStatus.getTurn().setCurrentPhase(CombatDamagePhase.CD);
        combatDamagePhase.next(gameStatus);
      }
    }
  }
}

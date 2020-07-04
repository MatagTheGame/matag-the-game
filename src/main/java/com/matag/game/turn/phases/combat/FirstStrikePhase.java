package com.matag.game.turn.phases.combat;

import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.action._continue.ConsolidateStatusService;
import com.matag.game.turn.action.combat.CombatService;
import com.matag.game.turn.phases.Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.type.AbilityType.DOUBLE_STRIKE;
import static com.matag.cards.ability.type.AbilityType.FIRST_STRIKE;
import static com.matag.game.turn.phases.combat.AfterFirstStrikePhase.AF;

@Component
@AllArgsConstructor
public class FirstStrikePhase implements Phase {
  public static final String FS = "FS";

  private final AutocontinueChecker autocontinueChecker;
  private final CombatService combatService;
  private final ConsolidateStatusService consolidateStatusService;
  private final AfterFirstStrikePhase afterFirstStrikePhase;
  private final CombatDamagePhase combatDamagePhase;

  @Override
  public void next(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

    boolean executePhase = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
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

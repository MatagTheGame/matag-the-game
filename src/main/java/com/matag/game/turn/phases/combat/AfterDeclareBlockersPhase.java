package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.cards.ability.type.AbilityType.DOUBLE_STRIKE;
import static com.matag.cards.ability.type.AbilityType.FIRST_STRIKE;

@Component
public class AfterDeclareBlockersPhase extends AbstractPhase {
  public static final String AB = "AB";

  private final AutocontinueChecker autocontinueChecker;

  @Autowired
  private FirstStrikePhase firstStrikePhase;

  @Autowired
  private CombatDamagePhase combatDamagePhase;

  public AfterDeclareBlockersPhase(AutocontinueChecker autocontinueChecker) {
    this.autocontinueChecker = autocontinueChecker;
  }

  @Override
  public String getName() {
    return AB;
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

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.isCurrentPlayerActive()) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        next(gameStatus);
      }

    } else {
      var nextPhase = getNextPhase(gameStatus);
      gameStatus.getTurn().setCurrentPhase(nextPhase.getName());
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      gameStatus.getTurn().setPhaseActioned(false);
      getNextPhase(gameStatus).next(gameStatus);
    }
  }
}

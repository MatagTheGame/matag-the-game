package com.matag.game.turn.phases.combat;

import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.main2.Main2Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;

@Component
@AllArgsConstructor
public class BeginCombatPhase extends AbstractPhase {
  public static final String BC = "BC";

  private final AutocontinueChecker autocontinueChecker;

  @Override
  public String getName() {
    return BC;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    // TODO implement
    return null;
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      if (new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards()).canAnyCreatureAttack()) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

        if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
          next(gameStatus);
        }

      } else {
        gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(DA);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    }
  }
}

package com.matag.game.turn.phases;

import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;

@Component
@AllArgsConstructor
public class BeginCombatPhase implements Phase {
  public static final String BC = "BC";

  private final AutocontinueChecker autocontinueChecker;

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      if (new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards()).canAnyCreatureAttack()) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

        if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
          apply(gameStatus);
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

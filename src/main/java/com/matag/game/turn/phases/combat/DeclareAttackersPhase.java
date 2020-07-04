package com.matag.game.turn.phases.combat;

import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.main2.Main2Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeclareAttackersPhase extends AbstractPhase {
  public static final String DA = "DA";

  private final AutocontinueChecker autocontinueChecker;
  private final AfterDeclareBlockersPhase afterDeclareBlockersPhase;

  @Override
  public String getName() {
    return DA;
  }

  @Override
  public void next(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        next(gameStatus);
      }

    } else {
      if (!gameStatus.getCurrentPlayer().getBattlefield().getAttackingCreatures().isEmpty()) {
        if (new CardInstanceSearch(gameStatus.getNonCurrentPlayer().getBattlefield().getCards()).canAnyCreatureBlock()) {
          gameStatus.getTurn().setCurrentPhase(DeclareBlockersPhase.DB);
          gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

        } else {
          gameStatus.getTurn().setCurrentPhase(AfterDeclareBlockersPhase.AB);
          gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());

          if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
            afterDeclareBlockersPhase.next(gameStatus);
          }
        }
      } else {
        gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      }
    }
  }
}

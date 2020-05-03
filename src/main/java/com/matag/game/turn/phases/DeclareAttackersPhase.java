package com.matag.game.turn.phases;

import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeclareAttackersPhase implements Phase {
  public static final String DA = "DA";

  private final AutocontinueChecker autocontinueChecker;
  private final AfterDeclareBlockersPhase afterDeclareBlockersPhase;

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        apply(gameStatus);
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
            afterDeclareBlockersPhase.apply(gameStatus);
          }
        }
      } else {
        gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      }
    }
  }
}

package com.matag.game.turn.phases;

import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeclareAttackersPhase implements Phase {
  public static final String DA = "DA";

  private final FirstStrikePhase firstStrikePhase;

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

    } else {
      if (!gameStatus.getCurrentPlayer().getBattlefield().getAttackingCreatures().isEmpty() && new CardInstanceSearch(gameStatus.getNonCurrentPlayer().getBattlefield().getCards()).canAnyCreatureBlock()) {
        gameStatus.getTurn().setCurrentPhase(DeclareBlockersPhase.DB);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      } else {
        gameStatus.getTurn().setCurrentPhase(FirstStrikePhase.FS);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        firstStrikePhase.apply(gameStatus);
      }
    }
  }
}

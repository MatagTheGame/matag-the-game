package com.matag.game.turn.phases;

import com.matag.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;

@Component
public class BeginCombatPhase implements Phase {
  public static final String BC = "BC";

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      if (new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards()).canAnyCreatureAttack()) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      } else {
        gameStatus.getTurn().setCurrentPhase(Main2Phase.M2);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(DA);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    }
  }
}

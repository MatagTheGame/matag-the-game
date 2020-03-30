package com.matag.game.turn.phases;

import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.CleanupPhase.CL;

@Component
@AllArgsConstructor
public class EndTurnPhase implements Phase {
  public static final String ET = "ET";

  private final CleanupPhase cleanupPhase;

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getCurrentPlayer().getHand().size() > 7) {
      if (gameStatus.getTurn().getTriggeredNonStackAction() == null) {
        gameStatus.getTurn().setTriggeredNonStackAction("DISCARD_A_CARD");
      } else {
        throw new MessageException("Choose a card to discard.");
      }

    } else {
      if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      } else {
        gameStatus.getTurn().setCurrentPhase(CL);
        gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
        cleanupPhase.apply(gameStatus);
      }
    }
  }
}

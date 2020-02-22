package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.game.turn.phases.CleanupPhase.CL;

@Component
public class EndTurnPhase implements Phase {
  public static final String ET = "ET";

  private final CleanupPhase cleanupPhase;

  @Autowired
  public EndTurnPhase(CleanupPhase cleanupPhase) {
    this.cleanupPhase = cleanupPhase;
  }

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

package com.matag.game.turn.phases;

import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.FirstStrikePhase.FS;

@Component
public class AfterDeclareBlockersPhase implements Phase {
  public static final String AB = "AB";

  private final FirstStrikePhase firstStrikePhase;

  public AfterDeclareBlockersPhase(FirstStrikePhase firstStrikePhase) {
    this.firstStrikePhase = firstStrikePhase;
  }

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

    } else {
      gameStatus.getTurn().setCurrentPhase(FS);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      firstStrikePhase.apply(gameStatus);
    }
  }
}

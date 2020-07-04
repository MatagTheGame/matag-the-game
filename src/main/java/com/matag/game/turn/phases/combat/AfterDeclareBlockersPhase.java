package com.matag.game.turn.phases.combat;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.phases.Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.combat.FirstStrikePhase.FS;

@Component
@AllArgsConstructor
public class AfterDeclareBlockersPhase implements Phase {
  public static final String AB = "AB";

  private final AutocontinueChecker autocontinueChecker;
  private final FirstStrikePhase firstStrikePhase;

  @Override
  public void apply(GameStatus gameStatus) {
    if (gameStatus.getTurn().getCurrentPhaseActivePlayer().equals(gameStatus.getCurrentPlayer().getName())) {
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getNonCurrentPlayer().getName());

      if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
        apply(gameStatus);
      }

    } else {
      gameStatus.getTurn().setCurrentPhase(FS);
      gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
      firstStrikePhase.apply(gameStatus);
    }
  }
}

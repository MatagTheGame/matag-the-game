package com.matag.game.turn.phases.main2;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.ending.EndTurnPhase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Main2Phase extends AbstractPhase {
  public static final String M2 = "M2";

  private final EndTurnPhase endTurnPhase;

  @Override
  public String getName() {
    return M2;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return endTurnPhase;
  }

  @Override
  public void next(GameStatus gameStatus) {
    gameStatus.getTurn().setCurrentPhase(EndTurnPhase.ET);
    gameStatus.getTurn().setCurrentPhaseActivePlayer(gameStatus.getCurrentPlayer().getName());
    endTurnPhase.next(gameStatus);
  }
}

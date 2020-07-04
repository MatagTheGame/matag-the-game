package com.matag.game.turn.phases.ending;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.beginning.UntapPhase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.phases.beginning.UntapPhase.UT;

@Component
@AllArgsConstructor
public class CleanupPhase extends AbstractPhase {
  public static final String CL = "CL";

  private final UntapPhase untapPhase;

  @Override
  public String getName() {
    return CL;
  }

  @Override
  public void next(GameStatus gameStatus) {
    cleanup(gameStatus);
    moveToNextPlayer(gameStatus);
    untapPhase.next(gameStatus);
  }

  private void cleanup(GameStatus gameStatus) {
    gameStatus.getTurn().getCardsPlayedWithinTurn().clear();
    gameStatus.getAllBattlefieldCards().forEach(CardInstance::cleanup);
  }

  private void moveToNextPlayer(GameStatus gameStatus) {
    gameStatus.getTurn().increaseTurnNumber();
    gameStatus.getTurn().setCurrentPhase(UT);
    String nextCurrentPlayer = gameStatus.getNonCurrentPlayer().getName();
    gameStatus.getTurn().setCurrentTurnPlayer(nextCurrentPlayer);
    gameStatus.getTurn().setCurrentPhaseActivePlayer(nextCurrentPlayer);
  }
}

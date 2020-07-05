package com.matag.game.turn.phases.ending;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.beginning.UntapPhase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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
  public Phase getNextPhase(GameStatus gameStatus) {
    return untapPhase;
  }

  @Override
  public void action(GameStatus gameStatus) {
    super.action(gameStatus);
    gameStatus.getTurn().getCardsPlayedWithinTurn().clear();
    gameStatus.getAllBattlefieldCards().forEach(CardInstance::cleanup);
  }
}

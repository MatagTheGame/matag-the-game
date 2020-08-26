package com.matag.game.turn.phases.ending;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.beginning.UntapPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.matag.game.turn.action._continue.InputRequiredActions.DISCARD_A_CARD;

@Component
public class CleanupPhase extends AbstractPhase {
  public static final String CL = "CL";

  @Autowired
  private UntapPhase untapPhase;

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

    var handSize = gameStatus.getCurrentPlayer().getHand().size();
    if (handSize > 7) {
      if (gameStatus.getTurn().getInputRequiredAction() == null) {
        gameStatus.getTurn().setInputRequiredAction(DISCARD_A_CARD);
        gameStatus.getTurn().setInputRequiredActionParameter(String.valueOf(handSize - 7));
      }

    } else {
      gameStatus.getTurn().getCardsPlayedWithinTurn().clear();
      gameStatus.getAllBattlefieldCards().forEach(CardInstance::cleanup);
    }
  }
}

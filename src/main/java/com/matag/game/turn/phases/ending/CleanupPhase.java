package com.matag.game.turn.phases.ending;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.player.DiscardXCardsService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.beginning.UntapPhase;

@Component
public class CleanupPhase extends AbstractPhase {
  public static final String CL = "CL";

  @Autowired
  private UntapPhase untapPhase;

  @Autowired
  private DiscardXCardsService discardXCardsService;

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
        discardXCardsService.discardXCardsTrigger(gameStatus, handSize - 7);
      }

    } else {
      gameStatus.getTurn().getCardsPlayedWithinTurn().clear();
      gameStatus.getAllBattlefieldCards().forEach(CardInstance::cleanup);
    }
  }
}

package com.matag.game.turn.phases.ending;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.action.player.DiscardXCardsService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import org.springframework.stereotype.Component;

@Component
public class CleanupPhase extends AbstractPhase {
  public static final String CL = "CL";

  private DiscardXCardsService discardXCardsService;

    public CleanupPhase(AutocontinueChecker autocontinueChecker, DiscardXCardsService discardXCardsService) {
        super(autocontinueChecker);
        this.discardXCardsService = discardXCardsService;
    }

    @Override
  public String getName() {
    return CL;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return null;
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

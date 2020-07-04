package com.matag.game.turn.phases.beginning;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.action.tap.TapPermanentService;
import com.matag.game.turn.phases.AbstractPhase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UntapPhase extends AbstractPhase {
  public static final String UT = "UT";

  private final TapPermanentService tapPermanentService;
  private final AutocontinueChecker autocontinueChecker;
  private final UpkeepPhase upkeepPhase;

  @Override
  public String getName() {
    return UT;
  }

  @Override
  public void action(GameStatus gameStatus) {
    super.action(gameStatus);
    List<CardInstance> cards = gameStatus.getCurrentPlayer().getBattlefield().getCards();

    for (CardInstance cardInstance : new CardInstanceSearch(cards).tapped().getCards()) {
      if (cardInstance.getModifiers().isDoesNotUntapNextTurn()) {
        cardInstance.getModifiers().setDoesNotUntapNextTurn(false);
      } else {
        tapPermanentService.untap(gameStatus, cardInstance.getId());
      }
    }

    new CardInstanceSearch(cards).withSummoningSickness().getCards()
      .forEach(cardInstance -> cardInstance.getModifiers().setSummoningSickness(false));
  }

  @Override
  public void next(GameStatus gameStatus) {
    super.next(gameStatus);
    gameStatus.getTurn().setCurrentPhase(UpkeepPhase.UP);
    if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
      upkeepPhase.next(gameStatus);
    }
  }
}

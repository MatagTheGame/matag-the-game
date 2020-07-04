package com.matag.game.turn.phases.beginning;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
import com.matag.game.turn.action.tap.TapPermanentService;
import com.matag.game.turn.phases.Phase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UntapPhase implements Phase {
  public static final String UT = "UT";

  private final TapPermanentService tapPermanentService;
  private final AutocontinueChecker autocontinueChecker;
  private final UpkeepPhase upkeepPhase;

  @Override
  public void apply(GameStatus gameStatus) {
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

    gameStatus.getTurn().setCurrentPhase(UpkeepPhase.UP);

    if (!autocontinueChecker.canPerformAnyAction(gameStatus)) {
      upkeepPhase.apply(gameStatus);
    }
  }
}

package com.matag.game.turn.phases.beginning;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.tap.TapPermanentService;
import com.matag.game.turn.phases.AbstractPhase;
import com.matag.game.turn.phases.Phase;
import com.matag.game.turn.phases.PhaseUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UntapPhase extends AbstractPhase {
  public static final String UT = "UT";

  private final TapPermanentService tapPermanentService;
  private final UpkeepPhase upkeepPhase;

  @Override
  public String getName() {
    return UT;
  }

  @Override
  public Phase getNextPhase(GameStatus gameStatus) {
    return upkeepPhase;
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
}

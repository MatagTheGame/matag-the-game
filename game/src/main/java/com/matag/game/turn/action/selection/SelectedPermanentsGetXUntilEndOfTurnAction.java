package com.matag.game.turn.action.selection;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.cardinstance.ability.AbilityAction;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.permanent.PermanentService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelectedPermanentsGetXUntilEndOfTurnAction implements AbilityAction {

  private final CardInstanceSelectorService cardInstanceSelectorService;
  private final PermanentService permanentService;

  public SelectedPermanentsGetXUntilEndOfTurnAction(CardInstanceSelectorService cardInstanceSelectorService, PermanentService permanentService) {
    this.cardInstanceSelectorService = cardInstanceSelectorService;
    this.permanentService = permanentService;
  }

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    List<CardInstance> cards = cardInstanceSelectorService.select(gameStatus, cardInstance, ability.getCardInstanceSelector()).getCards();
    for (CardInstance card : cards) {
      permanentService.thatPermanentGets(cardInstance, gameStatus, ability.getParameters(), card);
    }
  }

}

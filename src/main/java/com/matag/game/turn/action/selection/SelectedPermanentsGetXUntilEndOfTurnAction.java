package com.matag.game.turn.action.selection;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.AbilityAction;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.permanent.PermanentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SelectedPermanentsGetXUntilEndOfTurnAction implements AbilityAction {
  private final MagicInstanceSelectorService magicInstanceSelectorService;
  private final PermanentService permanentService;

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability) {
    List<CardInstance> cards = magicInstanceSelectorService.select(gameStatus, cardInstance, ability.getMagicInstanceSelector()).getCards();
    for (CardInstance card : cards) {
      permanentService.thatPermanentGets(cardInstance, gameStatus, ability.getParameters(), card);
    }
  }
}

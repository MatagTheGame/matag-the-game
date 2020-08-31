package com.matag.game.turn.action.player;

import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.leave.PutIntoGraveyardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.matag.game.turn.action._continue.InputRequiredActions.DISCARD_A_CARD;
import static java.lang.Integer.parseInt;

@Component
@AllArgsConstructor
public class DiscardXCardsService {
  private final PutIntoGraveyardService putIntoGraveyardService;

  public void discardXCardsTrigger(GameStatus gameStatus, int cardsToDiscard) {
    gameStatus.getTurn().setInputRequiredAction(DISCARD_A_CARD);
    gameStatus.getTurn().setInputRequiredActionParameter(String.valueOf(cardsToDiscard));
  }

  public void discardXCards(List<Integer> discardedCardsIds, GameStatus gameStatus) {
    try {
      int cardsToDiscard = parseInt(gameStatus.getTurn().getInputRequiredActionParameter());
      var cards = gameStatus.getCurrentPlayer().getHand().extractCardsByIds(discardedCardsIds);

      if (cards.size() != cardsToDiscard) {
        throw new MessageException("Needs discarding " + cardsToDiscard + " cards, not " + cards.size());
      }

      putIntoGraveyardService.putIntoGraveyard(gameStatus, cards);

    } catch (Exception e) {
      throw new MessageException(e.getMessage());
    }
  }
}

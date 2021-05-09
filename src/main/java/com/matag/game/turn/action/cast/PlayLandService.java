package com.matag.game.turn.action.cast;

import org.springframework.stereotype.Component;

import com.matag.cards.Card;
import com.matag.cards.properties.Type;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService;
import com.matag.game.turn.phases.PhaseUtils;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PlayLandService {
  private final EnterCardIntoBattlefieldService enterCardIntoBattlefieldService;

  public void playLand(GameStatus gameStatus, int cardId) {
    var turn = gameStatus.getTurn();
    var activePlayer = gameStatus.getActivePlayer();

    if (!PhaseUtils.isMainPhase(turn.getCurrentPhase())) {
      throw new MessageException("You can only play lands during main phases.");

    } else if (turn.getCardsPlayedWithinTurn().stream()
      .map(CardInstance::getCard)
      .map(Card::getTypes)
      .anyMatch(types -> types.contains(Type.LAND))) {
      throw new MessageException("You already played a land this turn.");

    } else {
      var cardInstance = activePlayer.getHand().findCardById(cardId);
      if (cardInstance.isOfType(Type.LAND)) {
        cardInstance = activePlayer.getHand().extractCardById(cardId);
        cardInstance.setController(activePlayer.getName());
        turn.addCardToCardsPlayedWithinTurn(cardInstance);
        enterCardIntoBattlefieldService.enter(gameStatus, cardInstance);

      } else {
        throw new MessageException("Playing " + cardInstance.getIdAndName() + " as land.");
      }
    }
  }

}

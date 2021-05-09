package com.matag.game.turn.action.leave;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;

@Component
public class PutIntoHandService {
  public void returnToHand(GameStatus gameStatus, CardInstance cardInstance) {
    var owner = gameStatus.getPlayerByName(cardInstance.getOwner());
    cardInstance.resetAllModifiers();
    owner.getHand().addCard(cardInstance);
  }
}

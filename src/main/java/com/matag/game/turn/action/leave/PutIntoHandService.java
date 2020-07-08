package com.matag.game.turn.action.leave;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PutIntoHandService {
  public void returnToHand(GameStatus gameStatus, CardInstance cardInstance) {
    var owner = gameStatus.getPlayerByName(cardInstance.getOwner());
    cardInstance.resetAllModifiers();
    owner.getHand().addCard(cardInstance);
  }
}

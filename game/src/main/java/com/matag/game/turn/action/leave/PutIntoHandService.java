package com.matag.game.turn.action.leave;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PutIntoHandService {
  public void returnToHand(GameStatus gameStatus, CardInstance cardInstance) {
    Player owner = gameStatus.getPlayerByName(cardInstance.getOwner());
    cardInstance.resetAllModifiers();
    owner.getHand().addCard(cardInstance);
  }
}

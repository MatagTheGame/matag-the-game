package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PutIntoHandService {
  public void returnToHand(GameStatus gameStatus, CardInstance cardInstance) {
    Player owner = gameStatus.getPlayerByName(cardInstance.getOwner());
    cardInstance.resetAllModifiers();
    owner.getHand().addCard(cardInstance);
  }
}

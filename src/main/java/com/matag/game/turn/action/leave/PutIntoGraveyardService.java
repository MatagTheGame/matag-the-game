package com.matag.game.turn.action.leave;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PutIntoGraveyardService {
  public void putIntoGraveyard(GameStatus gameStatus, CardInstance cardInstance) {
    var owner = gameStatus.getPlayerByName(cardInstance.getOwner());
    cardInstance.resetAllModifiers();
    owner.getGraveyard().addCard(cardInstance);
  }
}

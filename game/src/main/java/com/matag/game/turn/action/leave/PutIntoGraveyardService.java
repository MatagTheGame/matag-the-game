package com.matag.game.turn.action.leave;

import com.matag.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PutIntoGraveyardService {
  public void putIntoGraveyard(GameStatus gameStatus, CardInstance cardInstance) {
    Player owner = gameStatus.getPlayerByName(cardInstance.getOwner());
    cardInstance.resetAllModifiers();
    owner.getGraveyard().addCard(cardInstance);
  }
}

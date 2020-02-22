package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class PutIntoGraveyardService {
  public void putIntoGraveyard(GameStatus gameStatus, CardInstance cardInstance) {
    Player owner = gameStatus.getPlayerByName(cardInstance.getOwner());
    cardInstance.resetAllModifiers();
    owner.getGraveyard().addCard(cardInstance);
  }
}

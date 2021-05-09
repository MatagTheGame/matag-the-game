package com.matag.game.turn.action.leave;

import java.util.List;

import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;

@Component
public class PutIntoGraveyardService {
  public void putIntoGraveyard(GameStatus gameStatus, List<CardInstance> cards) {
    for (CardInstance card : cards) {
      var owner = gameStatus.getPlayerByName(card.getOwner());
      card.resetAllModifiers();
      owner.getGraveyard().addCard(card);
    }
  }

  public void putIntoGraveyard(GameStatus gameStatus, CardInstance cardInstance) {
    putIntoGraveyard(gameStatus, List.of(cardInstance));
  }
}

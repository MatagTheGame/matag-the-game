package com.matag.game.turn.action.player;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.matag.game.turn.action._continue.InputRequiredActions.SCRY;

@Component
@AllArgsConstructor
public class ScryXCardsService {
  private static final int MAX_CARDS_TO_SCRY = 5;

  public void scryXCardsTrigger(Player player, int cardsToScry, GameStatus gameStatus) {
    if (cardsToScry > 0) {
      gameStatus.getTurn().setInputRequiredAction(SCRY);
      gameStatus.getTurn().setInputRequiredActionParameter(String.valueOf(cardsToScry));
    }
  }

  public void scryXCards(GameStatus gameStatus, List<Integer> positions) {
    var cards = gameStatus.getCurrentPlayer().getLibrary().getCards();
    var extractedCards = new ArrayList<CardInstance>();
    for (int i = 0; i < positions.size(); i++) {
      extractedCards.add(cards.remove(0));
    }

    for (int i = 0; i < positions.size(); i++) {
      var position = positions.get(i);
      var card = extractedCards.get(i);
      for (int j = MAX_CARDS_TO_SCRY; j > 0; j--) {
        if (j == Math.abs(position)) {
          if (position > 0) {
            cards.add(0, card);
          } else {
            cards.add(card);
          }
        }
      }
    }
  }
}

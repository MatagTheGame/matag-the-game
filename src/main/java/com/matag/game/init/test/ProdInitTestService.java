package com.matag.game.init.test;

import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.cards.Cards;
import com.matag.game.status.GameStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class ProdInitTestService extends InitTestService {

  public ProdInitTestService(CardInstanceFactory cardInstanceFactory, Cards cards) {
    super.cardInstanceFactory = cardInstanceFactory;
    super.cards = cards;
  }

  @Override
  public void initGameStatus(GameStatus gameStatus) {
    // Current Player
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));
    addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));

    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
    addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));

    addCardToCurrentPlayerHand(gameStatus, cards.get("Sunscorched Desert"));

    addCardToCurrentPlayerGraveyard(gameStatus, cards.get("Plains"));

    // Non Current Player
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Forest"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Forest"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Forest"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
    addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));

    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"));
    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"));

    addCardToNonCurrentPlayerGraveyard(gameStatus, cards.get("Mountain"));
  }
}

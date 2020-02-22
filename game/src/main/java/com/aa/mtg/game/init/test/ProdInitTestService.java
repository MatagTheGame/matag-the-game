package com.aa.mtg.game.init.test;

import com.aa.mtg.game.status.GameStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class ProdInitTestService extends InitTestService {
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

    addCardToCurrentPlayerHand(gameStatus, cards.get("Huatli's Snubhorn"));
    addCardToCurrentPlayerHand(gameStatus, cards.get("Legion's Judgment"));
    addCardToCurrentPlayerHand(gameStatus, cards.get("Air Elemental"));
    addCardToCurrentPlayerHand(gameStatus, cards.get("Clear the Mind"));
    addCardToCurrentPlayerHand(gameStatus, cards.get("Civic Stalwart"));
    addCardToCurrentPlayerHand(gameStatus, cards.get("Candlelight Vigil"));
    addCardToCurrentPlayerHand(gameStatus, cards.get("Overflowing Insight"));

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
    addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"));

    addCardToNonCurrentPlayerHand(gameStatus, cards.get("Gilded Sentinel"));
    addCardToNonCurrentPlayerHand(gameStatus, cards.get("Charging Monstrosaur"));
    addCardToNonCurrentPlayerHand(gameStatus, cards.get("Precision Bold"));
    addCardToNonCurrentPlayerHand(gameStatus, cards.get("Overcome"));
    addCardToNonCurrentPlayerHand(gameStatus, cards.get("Marauders Axe"));
    addCardToNonCurrentPlayerHand(gameStatus, cards.get("Dead Weight"));

    addCardToNonCurrentPlayerGraveyard(gameStatus, cards.get("Mountain"));
  }
}

package com.aa.mtg.game.init;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import com.aa.mtg.game.turn.TurnController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.Ixalan.AIR_ELEMENTAL;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.cards.sets.Ixalan.HUATLIS_SNUBHORN;
import static com.aa.mtg.cards.sets.Ixalan.LEGIONS_JUDGMENT;

@Profile("test")
@Service
public class InitTestService {
    private Logger LOGGER = LoggerFactory.getLogger(TurnController.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public InitTestService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    void initGameStatusForTest(GameStatus gameStatus) {
        LOGGER.warn("Application is running in test mode: Initializing the gameStatus with test data");

        // Clear
        gameStatus.getCurrentPlayer().getLibrary().getCards().clear();
        gameStatus.getCurrentPlayer().getHand().getCards().clear();
        gameStatus.getNonCurrentPlayer().getLibrary().getCards().clear();
        gameStatus.getNonCurrentPlayer().getHand().getCards().clear();

        // Current Player
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToCurrentPlayerLibrary(gameStatus, ISLAND);
        addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

        addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
        addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
        addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);

        addCardToCurrentPlayerHand(gameStatus, ISLAND);
        addCardToCurrentPlayerHand(gameStatus, PLAINS);
        addCardToCurrentPlayerHand(gameStatus, HUATLIS_SNUBHORN);
        addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);
        addCardToCurrentPlayerHand(gameStatus, AIR_ELEMENTAL);
        addCardToCurrentPlayerHand(gameStatus, AIR_ELEMENTAL);

        gameStatusUpdaterService.sendUpdateCurrentPlayerHand(gameStatus);
        gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
        gameStatusUpdaterService.sendUpdateCurrentPlayerLibrarySize(gameStatus);

        // Non Current Player
        addCardToNonCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToNonCurrentPlayerLibrary(gameStatus, PLAINS);
        addCardToNonCurrentPlayerLibrary(gameStatus, ISLAND);
        addCardToNonCurrentPlayerLibrary(gameStatus, ISLAND);

        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);
        addCardToNonCurrentPlayerBattlefield(gameStatus, FOREST);

        addCardToNonCurrentPlayerHand(gameStatus, MOUNTAIN);
        addCardToNonCurrentPlayerHand(gameStatus, FOREST);
        addCardToNonCurrentPlayerHand(gameStatus, GRAZING_WHIPTAIL);
        addCardToNonCurrentPlayerHand(gameStatus, GRAZING_WHIPTAIL);

        gameStatusUpdaterService.sendUpdateNonCurrentPlayerHand(gameStatus);
        gameStatusUpdaterService.sendUpdateNonCurrentPlayerBattlefield(gameStatus);
        gameStatusUpdaterService.sendUpdateNonCurrentPlayerLibrarySize(gameStatus);
    }

    private void addCardToCurrentPlayerLibrary(GameStatus gameStatus, Card card) {
        gameStatus.getCurrentPlayer().getLibrary().addCard(new CardInstance(gameStatus.nextCardId(), card, gameStatus.getCurrentPlayer().getName()));
    }

    private void addCardToCurrentPlayerHand(GameStatus gameStatus, Card card) {
        gameStatus.getCurrentPlayer().getHand().addCard(new CardInstance(gameStatus.nextCardId(), card, gameStatus.getCurrentPlayer().getName()));
    }

    private void addCardToCurrentPlayerBattlefield(GameStatus gameStatus, Card card) {
        gameStatus.getCurrentPlayer().getBattlefield().addCard(new CardInstance(gameStatus.nextCardId(), card, gameStatus.getCurrentPlayer().getName()));
    }

    private void addCardToNonCurrentPlayerLibrary(GameStatus gameStatus, Card card) {
        gameStatus.getNonCurrentPlayer().getLibrary().addCard(new CardInstance(gameStatus.nextCardId(), card, gameStatus.getNonCurrentPlayer().getName()));
    }

    private void addCardToNonCurrentPlayerHand(GameStatus gameStatus, Card card) {
        gameStatus.getNonCurrentPlayer().getHand().addCard(new CardInstance(gameStatus.nextCardId(), card, gameStatus.getNonCurrentPlayer().getName()));
    }

    private void addCardToNonCurrentPlayerBattlefield(GameStatus gameStatus, Card card) {
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(new CardInstance(gameStatus.nextCardId(), card, gameStatus.getNonCurrentPlayer().getName()));
    }
}

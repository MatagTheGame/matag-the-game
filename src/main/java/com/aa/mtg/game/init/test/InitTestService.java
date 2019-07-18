package com.aa.mtg.game.init.test;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class InitTestService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitTestService.class);

    public void initGameStatusForTest(GameStatus gameStatus) {
        LOGGER.warn("Application is running in test mode: Initializing the gameStatus with test data.");

        // Clear
        gameStatus.getCurrentPlayer().getLibrary().getCards().clear();
        gameStatus.getCurrentPlayer().getHand().getCards().clear();
        gameStatus.getNonCurrentPlayer().getLibrary().getCards().clear();
        gameStatus.getNonCurrentPlayer().getHand().getCards().clear();

        // Call abstract initStatus
        initGameStatus(gameStatus);
    }

    public abstract void initGameStatus(GameStatus gameStatus);

    protected void addCardToCurrentPlayerLibrary(GameStatus gameStatus, Card card) {
        gameStatus.getCurrentPlayer().getLibrary().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getCurrentPlayer().getName()));
    }

    protected void addCardToCurrentPlayerHand(GameStatus gameStatus, Card card) {
        gameStatus.getCurrentPlayer().getHand().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getCurrentPlayer().getName()));
    }

    protected void addCardToCurrentPlayerBattlefield(GameStatus gameStatus, Card card) {
        gameStatus.getCurrentPlayer().getBattlefield().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getCurrentPlayer().getName()));
    }

    protected void addCardToCurrentPlayerGraveyard(GameStatus gameStatus, Card card) {
        gameStatus.getCurrentPlayer().getGraveyard().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getCurrentPlayer().getName()));
    }

    protected void addCardToNonCurrentPlayerLibrary(GameStatus gameStatus, Card card) {
        gameStatus.getNonCurrentPlayer().getLibrary().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getNonCurrentPlayer().getName()));
    }

    protected void addCardToNonCurrentPlayerHand(GameStatus gameStatus, Card card) {
        gameStatus.getNonCurrentPlayer().getHand().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getNonCurrentPlayer().getName()));
    }

    protected void addCardToNonCurrentPlayerBattlefield(GameStatus gameStatus, Card card) {
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getNonCurrentPlayer().getName()));
    }

    protected void addCardToNonCurrentPlayerGraveyard(GameStatus gameStatus, Card card) {
        gameStatus.getNonCurrentPlayer().getGraveyard().addCard(new CardInstance(gameStatus, gameStatus.nextCardId(), card, gameStatus.getNonCurrentPlayer().getName()));
    }
}

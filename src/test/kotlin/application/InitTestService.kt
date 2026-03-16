package application

import com.matag.cards.Card
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.init.InitService
import com.matag.game.status.GameStatus

abstract class InitTestService(
    protected val cardInstanceFactory: CardInstanceFactory,
    protected val cards: Cards
) : InitService {

    abstract fun initTestGameStatus(gameStatus: GameStatus)

    override fun initGameStatus(gameStatus: GameStatus) {
        gameStatus.currentPlayer.hand.extractAllCards()
        gameStatus.nonCurrentPlayer.hand.extractAllCards()
        initTestGameStatus(gameStatus)
    }

    protected fun addCardToCurrentPlayerLibrary(gameStatus: GameStatus, card: Card) {
        gameStatus.currentPlayer.library.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, gameStatus.currentPlayer.name)
        )
    }

    protected fun addCardToCurrentPlayerHand(gameStatus: GameStatus, card: Card) {
        gameStatus.currentPlayer.hand.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, gameStatus.currentPlayer.name)
        )
    }

    protected fun addCardToCurrentPlayerBattlefield(gameStatus: GameStatus, card: Card) {
        val owner = gameStatus.currentPlayer.name
        val controller = gameStatus.currentPlayer.name
        gameStatus.currentPlayer.battlefield.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, owner, controller)
        )
    }

    protected fun addCardToCurrentPlayerGraveyard(gameStatus: GameStatus, card: Card) {
        gameStatus.currentPlayer.graveyard.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, gameStatus.currentPlayer.name)
        )
    }

    protected fun addCardToNonCurrentPlayerLibrary(gameStatus: GameStatus, card: Card) {
        gameStatus.nonCurrentPlayer.library.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, gameStatus.nonCurrentPlayer.name)
        )
    }

    protected fun addCardToNonCurrentPlayerHand(gameStatus: GameStatus, card: Card) {
        gameStatus.nonCurrentPlayer.hand.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, gameStatus.nonCurrentPlayer.name)
        )
    }

    protected fun addCardToNonCurrentPlayerBattlefield(gameStatus: GameStatus, card: Card) {
        val owner = gameStatus.nonCurrentPlayer.name
        val controller = gameStatus.nonCurrentPlayer.name
        gameStatus.nonCurrentPlayer.battlefield.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, owner, controller)
        )
    }

    protected fun addCardToNonCurrentPlayerGraveyard(gameStatus: GameStatus, card: Card) {
        gameStatus.nonCurrentPlayer.graveyard.addCard(
            cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, gameStatus.nonCurrentPlayer.name)
        )
    }
}

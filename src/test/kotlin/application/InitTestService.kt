package application

import com.matag.cards.Card
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class InitTestService {
    protected var cardInstanceFactory: CardInstanceFactory? = null
    @JvmField
    var cards: Cards? = null

    fun initGameStatusForTest(gameStatus: GameStatus) {
        LOGGER.warn("Application is running in test mode: Initializing the gameStatus with test data.")

        // Clear
        gameStatus.currentPlayer.library.cards.clear()
        gameStatus.currentPlayer.hand.cards.clear()
        gameStatus.nonCurrentPlayer.library.cards.clear()
        gameStatus.nonCurrentPlayer.hand.cards.clear()

        // Call abstract initStatus
        initGameStatus(gameStatus)
    }

    abstract fun initGameStatus(gameStatus: GameStatus?)

    protected fun addCardToCurrentPlayerLibrary(gameStatus: GameStatus, card: Card?) {
        gameStatus.currentPlayer.library.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                gameStatus.nextCardId(),
                card,
                gameStatus.currentPlayer.name
            )
        )
    }

    protected fun addCardToCurrentPlayerHand(gameStatus: GameStatus, card: Card?) {
        gameStatus.currentPlayer.hand.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                gameStatus.nextCardId(),
                card,
                gameStatus.currentPlayer.name
            )
        )
    }

    protected fun addCardToCurrentPlayerBattlefield(gameStatus: GameStatus, card: Card?) {
        val owner: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
            gameStatus.currentPlayer.name
        val controller: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
            gameStatus.currentPlayer.name
        gameStatus.currentPlayer.battlefield
            .addCard(cardInstanceFactory!!.create(gameStatus, gameStatus.nextCardId(), card, owner, controller))
    }

    protected fun addCardToCurrentPlayerGraveyard(gameStatus: GameStatus, card: Card?) {
        gameStatus.currentPlayer.graveyard.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                gameStatus.nextCardId(),
                card,
                gameStatus.currentPlayer.name
            )
        )
    }

    protected fun addCardToNonCurrentPlayerLibrary(gameStatus: GameStatus, card: Card?) {
        gameStatus.nonCurrentPlayer.library.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                gameStatus.nextCardId(),
                card,
                gameStatus.nonCurrentPlayer.name
            )
        )
    }

    protected fun addCardToNonCurrentPlayerHand(gameStatus: GameStatus, card: Card?) {
        gameStatus.nonCurrentPlayer.hand.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                gameStatus.nextCardId(),
                card,
                gameStatus.nonCurrentPlayer.name
            )
        )
    }

    protected fun addCardToNonCurrentPlayerBattlefield(gameStatus: GameStatus, card: Card?) {
        val owner: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
            gameStatus.nonCurrentPlayer.name
        val controller: Unit /* TODO: class org.jetbrains.kotlin.nj2k.types.JKJavaNullPrimitiveType */? =
            gameStatus.nonCurrentPlayer.name
        gameStatus.nonCurrentPlayer.battlefield
            .addCard(cardInstanceFactory!!.create(gameStatus, gameStatus.nextCardId(), card, owner, controller))
    }

    protected fun addCardToNonCurrentPlayerGraveyard(gameStatus: GameStatus, card: Card?) {
        gameStatus.nonCurrentPlayer.graveyard.addCard(
            cardInstanceFactory!!.create(
                gameStatus,
                gameStatus.nextCardId(),
                card,
                gameStatus.nonCurrentPlayer.name
            )
        )
    }

    fun setCardInstanceFactory(cardInstanceFactory: CardInstanceFactory) {
        this.cardInstanceFactory = cardInstanceFactory
    }

    fun getCardInstanceFactory(): CardInstanceFactory {
        return cardInstanceFactory!!
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(InitTestService::class.java)
    }
}

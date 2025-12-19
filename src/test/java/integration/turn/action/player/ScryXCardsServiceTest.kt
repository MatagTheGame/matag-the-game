package integration.turn.action.player

import com.matag.cards.Card
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.player.ScryXCardsService
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.List
import java.util.stream.Collectors

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [PlayerTestConfiguration::class, TestUtilsConfiguration::class])
class ScryXCardsServiceTest {
    private var cardInstanceId = 60

    @Autowired
    private val scryXCardsService: ScryXCardsService? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Test
    fun scry1CardWithoutChangingOrder() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.getCurrentPlayer().getLibrary().getCards().clear()

        gameStatus.getCurrentPlayer().getLibrary().addCards(
            List.of<CardInstance?>(
                createCardInstance(gameStatus, "Plains"),
                createCardInstance(gameStatus, "Island"),
                createCardInstance(gameStatus, "Swamp"),
                createCardInstance(gameStatus, "Mountain"),
                createCardInstance(gameStatus, "Forest")
            )
        )

        // When
        scryXCardsService!!.scryXCards(gameStatus, mutableListOf<Int?>(1))

        // Then
        assertCards(gameStatus, "Plains", "Island", "Swamp", "Mountain", "Forest")
    }

    @Test
    fun scry1CardPutToBottom() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.getCurrentPlayer().getLibrary().getCards().clear()

        gameStatus.getCurrentPlayer().getLibrary().addCards(
            List.of<CardInstance?>(
                createCardInstance(gameStatus, "Plains"),
                createCardInstance(gameStatus, "Island"),
                createCardInstance(gameStatus, "Swamp"),
                createCardInstance(gameStatus, "Mountain"),
                createCardInstance(gameStatus, "Forest")
            )
        )

        // When
        scryXCardsService!!.scryXCards(gameStatus, List.of<Int?>(-1))

        // Then
        assertCards(gameStatus, "Island", "Swamp", "Mountain", "Forest", "Plains")
    }

    @Test
    fun scry4Cards() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.getCurrentPlayer().getLibrary().getCards().clear()

        gameStatus.getCurrentPlayer().getLibrary().addCards(
            List.of<CardInstance?>(
                createCardInstance(gameStatus, "Plains"),
                createCardInstance(gameStatus, "Island"),
                createCardInstance(gameStatus, "Swamp"),
                createCardInstance(gameStatus, "Mountain"),
                createCardInstance(gameStatus, "Forest")
            )
        )

        // When
        scryXCardsService!!.scryXCards(gameStatus, List.of<Int?>(2, -1, -2, 1))

        // Then
        assertCards(gameStatus, "Mountain", "Plains", "Forest", "Island", "Swamp")
    }

    private fun createCardInstance(gameStatus: GameStatus, cardName: String): CardInstance? {
        val card = cards!!.get(cardName)
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, ++cardInstanceId, card, "player-name", "player-name")
        gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance)
        return cardInstance
    }

    private fun assertCards(gameStatus: GameStatus, vararg cardNames: String?) {
        val actualCardNames = gameStatus.currentPlayer.library.getCards()
            .map { it.card.name }

        Assertions.assertThat(actualCardNames).containsExactly(*cardNames)
    }
}
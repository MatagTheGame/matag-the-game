package integration.turn.action.player

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.player.ScryXCardsService
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
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
        gameStatus.currentPlayer.library.cards = listOf()

        gameStatus.currentPlayer.library.addCards(
            listOf(
                createCardInstance(gameStatus, "Plains"),
                createCardInstance(gameStatus, "Island"),
                createCardInstance(gameStatus, "Swamp"),
                createCardInstance(gameStatus, "Mountain"),
                createCardInstance(gameStatus, "Forest")
            )
        )

        // When
        scryXCardsService!!.scryXCards(gameStatus, listOf(1))

        // Then
        assertCards(gameStatus, "Plains", "Island", "Swamp", "Mountain", "Forest")
    }

    @Test
    fun scry1CardPutToBottom() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.currentPlayer.library.cards = listOf()

        gameStatus.currentPlayer.library.addCards(
            listOf(
                createCardInstance(gameStatus, "Plains"),
                createCardInstance(gameStatus, "Island"),
                createCardInstance(gameStatus, "Swamp"),
                createCardInstance(gameStatus, "Mountain"),
                createCardInstance(gameStatus, "Forest")
            )
        )

        // When
        scryXCardsService!!.scryXCards(gameStatus, listOf(-1))

        // Then
        assertCards(gameStatus, "Island", "Swamp", "Mountain", "Forest", "Plains")
    }

    @Test
    fun scry4Cards() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        gameStatus.currentPlayer.library.cards = listOf()

        gameStatus.currentPlayer.library.addCards(
            listOf(
                createCardInstance(gameStatus, "Plains"),
                createCardInstance(gameStatus, "Island"),
                createCardInstance(gameStatus, "Swamp"),
                createCardInstance(gameStatus, "Mountain"),
                createCardInstance(gameStatus, "Forest")
            )
        )

        // When
        scryXCardsService!!.scryXCards(gameStatus, listOf(2, -1, -2, 1))

        // Then
        assertCards(gameStatus, "Mountain", "Plains", "Forest", "Island", "Swamp")
    }

    private fun createCardInstance(gameStatus: GameStatus, cardName: String): CardInstance {
        val card = cards!!.get(cardName)
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, ++cardInstanceId, card, "player-name", "player-name")
        gameStatus.activePlayer.battlefield.addCard(cardInstance)
        return cardInstance
    }

    private fun assertCards(gameStatus: GameStatus, vararg cardNames: String?) {
        val actualCardNames = gameStatus.currentPlayer.library.cards
            .map { it.card?.name }

        Assertions.assertThat(actualCardNames).containsExactly(*cardNames)
    }
}
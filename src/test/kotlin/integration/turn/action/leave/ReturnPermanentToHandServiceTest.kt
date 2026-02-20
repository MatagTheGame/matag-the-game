package integration.turn.action.leave

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.leave.ReturnPermanentToHandService
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
class ReturnPermanentToHandServiceTest {
    @Autowired
    private val returnPermanentToHandService: ReturnPermanentToHandService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun testMarkReturnToHand() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.player1!!.battlefield.addCard(cardInstance)

        // When
        returnPermanentToHandService!!.markAsToBeReturnedToHand(gameStatus, 61)

        // Then
        Assertions.assertThat(cardInstance.modifiers.modifiersUntilEndOfTurn.isToBeReturnedToHand).isTrue()
    }

    @Test
    fun testReturnToHand() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.player1!!.battlefield.addCard(cardInstance)

        // When
        returnPermanentToHandService!!.returnPermanentToHand(gameStatus, 61)

        // Then
        Assertions.assertThat(gameStatus.player1?.battlefield?.size()).isEqualTo(0)
        Assertions.assertThat(gameStatus.player1?.hand?.cards).contains(cardInstance)
    }
}
package integration.turn.action.leave

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.leave.ReturnPermanentToHandService
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [LeaveTestConfiguration::class])
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
        gameStatus.getPlayer1().battlefield.addCard(cardInstance)

        // When
        returnPermanentToHandService!!.markAsToBeReturnedToHand(gameStatus, 61)

        // Then
        Assertions.assertThat(cardInstance.modifiers.modifiersUntilEndOfTurn.isToBeReturnedToHand()).isTrue()
    }

    @Test
    fun testReturnToHand() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        gameStatus.getPlayer1().battlefield.addCard(cardInstance)

        // When
        returnPermanentToHandService!!.returnPermanentToHand(gameStatus, 61)

        // Then
        Assertions.assertThat(gameStatus.getPlayer1().battlefield.size()).isEqualTo(0)
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().hand.cards).contains(cardInstance)
    }
}
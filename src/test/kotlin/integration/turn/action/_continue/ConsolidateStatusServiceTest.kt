package integration.turn.action._continue

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action._continue.ConsolidateStatusService
import integration.TestUtils
import integration.turn.action.leave.LeaveTestConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ContinueTestConfiguration::class, LeaveTestConfiguration::class])
class ConsolidateStatusServiceTest {
    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val consolidateStatusService: ConsolidateStatusService? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Test
    fun consolidateShouldReturnACreatureToHandAndClearTheModifiers() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        cardInstance.modifiers.modifiersUntilEndOfTurn.setToBeReturnedToHand(true)
        cardInstance.modifiers.dealDamage(1)
        gameStatus.getCurrentPlayer().battlefield.addCard(cardInstance)

        // When
        consolidateStatusService!!.consolidate(gameStatus)

        // Then
        Assertions.assertThat<CardInstance?>(gameStatus.getCurrentPlayer().battlefield.cards).hasSize(0)
        Assertions.assertThat<CardInstance?>(gameStatus.getCurrentPlayer().hand.cards).contains(cardInstance)
        Assertions.assertThat(cardInstance.modifiers.damage).isEqualTo(0)
    }

    @Test
    fun consolidateShouldDestroyACreatureAndClearTheModifiers() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, 61, cards!!.get("Canopy Spider"), "player-name", "player-name")
        cardInstance.modifiers.modifiersUntilEndOfTurn.setToBeDestroyed(true)
        cardInstance.modifiers.dealDamage(1)
        gameStatus.getCurrentPlayer().battlefield.addCard(cardInstance)

        // When
        consolidateStatusService!!.consolidate(gameStatus)

        // Then
        Assertions.assertThat<CardInstance?>(gameStatus.getCurrentPlayer().battlefield.cards).hasSize(0)
        Assertions.assertThat<CardInstance?>(gameStatus.getCurrentPlayer().graveyard.cards)
            .contains(cardInstance)
        Assertions.assertThat(cardInstance.modifiers.damage).isEqualTo(0)
    }
}
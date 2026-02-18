package integration.turn.action._continue

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action._continue.ConsolidateStatusService
import integration.TestUtils
import integration.turn.action.leave.LeaveTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ContinueTestConfiguration::class, LeaveTestConfiguration::class])
class ConsolidateStatusServiceTest(
    private val testUtils: TestUtils,
    private val consolidateStatusService: ConsolidateStatusService,
    private val cards: Cards,
    private val cardInstanceFactory: CardInstanceFactory
) {
    
    @Test
    fun consolidateShouldReturnACreatureToHandAndClearTheModifiers() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name")
        cardInstance.modifiers.modifiersUntilEndOfTurn.isToBeReturnedToHand = true
        cardInstance.modifiers.dealDamage(1)
        gameStatus.currentPlayer.battlefield.addCard(cardInstance)

        // When
        consolidateStatusService.consolidate(gameStatus)

        // Then
        assertThat(gameStatus.currentPlayer.battlefield.cards).hasSize(0)
        assertThat(gameStatus.currentPlayer.hand.cards).contains(cardInstance)
        assertThat(cardInstance.modifiers.damage).isEqualTo(0)
    }

    @Test
    fun consolidateShouldDestroyACreatureAndClearTheModifiers() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name")
        cardInstance.modifiers.modifiersUntilEndOfTurn.isToBeDestroyed = true
        cardInstance.modifiers.dealDamage(1)
        gameStatus.currentPlayer.battlefield.addCard(cardInstance)

        // When
        consolidateStatusService.consolidate(gameStatus)

        // Then
        assertThat(gameStatus.currentPlayer.battlefield.cards).hasSize(0)
        assertThat(gameStatus.currentPlayer.graveyard.cards).contains(cardInstance)
        assertThat(cardInstance.modifiers.damage).isEqualTo(0)
    }
}
package integration.cardinstance.cost

import com.matag.cards.CardsConfiguration
import com.matag.game.cardinstance.cost.PayCostService
import com.matag.game.status.GameStatus
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@Import(CardsConfiguration::class, TestUtilsConfiguration::class)
class PayCostServiceTest(
    val testUtils: TestUtils,
    val payCostService: PayCostService
) {
    @Test
    fun `pay for casting Feral Maaka`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = addCardToPlayerBattlefield(gameStatus, "Feral Maaka")
        val mountain1 = addCardToPlayerBattlefield(gameStatus, "Mountain")
        val mountain2 = addCardToPlayerBattlefield(gameStatus, "Mountain")

        val manaPaid = mapOf(
            mountain1.id to listOf("RED"),
            mountain2.id to listOf("RED")
        )

        // When
        payCostService.pay(gameStatus, gameStatus.activePlayer, cardInstance, null, manaPaid)

        // Then
        assertThat(mountain1.modifiers.isTapped).isTrue
        assertThat(mountain2.modifiers.isTapped).isTrue
    }

    @Test
    fun `pay for casting tap target creation ability of Checkpoint Officer`() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = addCardToPlayerBattlefield(gameStatus, "Checkpoint Officer")
        val plains1 = addCardToPlayerBattlefield(gameStatus, "Plains")
        val plains2 = addCardToPlayerBattlefield(gameStatus, "Plains")
        val manaPaid = mapOf(
            plains1.id to listOf("WHITE"),
            plains2.id to listOf("WHITE")
        )

        // When
        payCostService.pay(gameStatus, gameStatus.activePlayer, cardInstance, "THAT_TARGETS_GET", manaPaid)

        // Then
        assertThat(plains1.modifiers.isTapped).isTrue
        assertThat(plains2.modifiers.isTapped).isTrue
        assertThat(cardInstance.modifiers.isTapped).isTrue
    }

    private fun addCardToPlayerBattlefield(gameStatus: GameStatus, cardName: String) =
        testUtils.createCardInstance(gameStatus, cardName).also {
            gameStatus.activePlayer.battlefield.addCard(it)
        }
}
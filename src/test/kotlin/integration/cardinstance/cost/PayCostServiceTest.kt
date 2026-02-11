package integration.cardinstance.cost

import com.matag.cards.CardsConfiguration
import com.matag.game.cardinstance.cost.PayCostService
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.tap.TapPermanentService
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Map

@ExtendWith(SpringExtension::class)
@Import(CardsConfiguration::class, TestUtilsConfiguration::class)
class PayCostServiceTest(
    @param:Autowired val testUtils: TestUtils,
    @param:Autowired val payCostService: PayCostService,
    @param:Autowired val tapPermanentService: TapPermanentService
) {
    @Test
    fun isCastingCostFulfilledFeralMaakaCorrectCosts() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = addCardToPlayerBattlefield(gameStatus, "Feral Maaka")
        val mountain1 = addCardToPlayerBattlefield(gameStatus, "Mountain")
        val mountain2 = addCardToPlayerBattlefield(gameStatus, "Mountain")

        val manaPaid: MutableMap<Int, List<String>> = Map.of<Int, List<String>>(
            mountain1.id, listOf("RED"),
            mountain2.id, listOf("RED")
        )

        // When
        payCostService.pay(gameStatus, gameStatus.activePlayer, cardInstance, null, manaPaid)

        // Then
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, mountain1.id)
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, mountain2.id)
    }

    @Test
    fun isCastingCostFulfilledCheckpointOfficerTapAbility() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val cardInstance = addCardToPlayerBattlefield(gameStatus, "Checkpoint Officer")
        val plains1 = addCardToPlayerBattlefield(gameStatus, "Plains")
        val plains2 = addCardToPlayerBattlefield(gameStatus, "Plains")
        val manaPaid: MutableMap<Int, List<String>> = Map.of<Int, List<String>>(
            plains1.id, listOf("WHITE"),
            plains2.id, listOf("WHITE")
        )

        // When
        payCostService.pay(gameStatus, gameStatus.activePlayer, cardInstance, "THAT_TARGETS_GET", manaPaid)

        // Then
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, plains1.id)
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, plains2.id)
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, cardInstance.id)
    }

    private fun addCardToPlayerBattlefield(gameStatus: GameStatus, cardName: String) =
        testUtils.createCardInstance(gameStatus, cardName).also {
            gameStatus.activePlayer.battlefield.addCard(it)
        }
}
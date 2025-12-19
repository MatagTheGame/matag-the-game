package integration.cardinstance.cost

import com.matag.cards.Cards
import com.matag.cards.CardsConfiguration
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
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
class PayCostServiceTest {
    private var cardInstanceId = 60

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val payCostService: PayCostService? = null

    @Autowired
    private val tapPermanentService: TapPermanentService? = null

    @Test
    fun isCastingCostFulfilledFeralMaakaCorrectCosts() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Feral Maaka")
        val mountain1 = createCardInstance(gameStatus, "Mountain")
        val mountain2 = createCardInstance(gameStatus, "Mountain")

        val manaPaid: MutableMap<Int, List<String>> = Map.of<Int, List<String>>(
            mountain1.getId(), listOf("RED"),
            mountain2.getId(), listOf("RED")
        )

        // When
        payCostService!!.pay(gameStatus, gameStatus.getActivePlayer(), cardInstance, null, manaPaid)

        // Then
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, mountain1.getId())
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, mountain2.getId())
    }

    @Test
    fun isCastingCostFulfilledCheckpointOfficerTapAbility() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Checkpoint Officer")
        val plains1 = createCardInstance(gameStatus, "Plains")
        val plains2 = createCardInstance(gameStatus, "Plains")
        val manaPaid: MutableMap<Int, List<String>> = Map.of<Int, List<String>>(
            plains1.getId(), listOf("WHITE"),
            plains2.getId(), listOf("WHITE")
        )

        // When
        payCostService!!.pay(gameStatus, gameStatus.getActivePlayer(), cardInstance, "THAT_TARGETS_GET", manaPaid)

        // Then
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, plains1.getId())
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, plains2.getId())
        Mockito.verify<TapPermanentService?>(tapPermanentService).tap(gameStatus, cardInstance.getId())
    }

    private fun createCardInstance(gameStatus: GameStatus, cardName: String): CardInstance {
        val card = cards!!.get(cardName)
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, ++cardInstanceId, card, "player-name", "player-name")
        gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance)
        return cardInstance
    }
}
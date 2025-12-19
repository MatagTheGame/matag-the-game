package integration.cardinstance.cost

import com.matag.cards.Cards
import com.matag.cards.CardsConfiguration
import com.matag.cards.properties.Cost
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.cardinstance.cost.CostService
import com.matag.game.status.GameStatus
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@Import(CardsConfiguration::class, TestUtilsConfiguration::class)
class CostServiceTest {
    private var cardInstanceId = 60

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val costService: CostService? = null

    @Test
    fun isCastingCostFulfilledFeralMaakaCorrectCosts() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Feral Maaka")
        val manaPaid = Arrays.asList<Cost?>(Cost.GREEN, Cost.RED)

        // When
        val fulfilled = costService!!.isCastingCostFulfilled(cardInstance, null, manaPaid)

        // Then
        Assertions.assertThat(fulfilled).isTrue()
    }

    @Test
    fun isCastingCostFulfilledFeralMaakaNoMana() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Feral Maaka")
        val manaPaid = ArrayList<Cost?>()

        // When
        val fulfilled = costService!!.isCastingCostFulfilled(cardInstance, null, manaPaid)

        // Then
        Assertions.assertThat(fulfilled).isFalse()
    }

    @Test
    fun isCastingCostFulfilledFeralMaakaWrongCost() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Feral Maaka")
        val manaPaid = Arrays.asList<Cost?>(Cost.WHITE, Cost.GREEN)

        // When
        val fulfilled = costService!!.isCastingCostFulfilled(cardInstance, null, manaPaid)

        // Then
        Assertions.assertThat(fulfilled).isFalse()
    }

    @Test
    fun isCastingCostFulfilledFeralMaakaOneLessMana() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Feral Maaka")
        val manaPaid = mutableListOf<Cost?>(Cost.RED)

        // When
        val fulfilled = costService!!.isCastingCostFulfilled(cardInstance, null, manaPaid)

        // Then
        Assertions.assertThat(fulfilled).isFalse()
    }

    @Test
    fun isCastingCostFulfilledAxebaneBeastCorrectCosts() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Axebane Beast")
        val manaPaid = Arrays.asList<Cost?>(Cost.GREEN, Cost.GREEN, Cost.RED, Cost.RED)

        // When
        val fulfilled = costService!!.isCastingCostFulfilled(cardInstance, null, manaPaid)

        // Then
        Assertions.assertThat(fulfilled).isTrue()
    }

    @Test
    fun isCastingCostFulfilledCheckpointOfficerTapAbility() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Checkpoint Officer")
        val manaPaid = Arrays.asList<Cost?>(Cost.WHITE, Cost.WHITE)

        // When
        val fulfilled = costService!!.isCastingCostFulfilled(cardInstance, "THAT_TARGETS_GET", manaPaid)

        // Then
        Assertions.assertThat(fulfilled).isTrue()
    }

    @Test
    fun isCastingCostFulfilledCheckpointOfficerTapAbilityOfTappedCreature() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Checkpoint Officer")
        cardInstance.getModifiers().setTapped(true)
        val manaPaid = Arrays.asList<Cost?>(Cost.WHITE, Cost.WHITE)

        // When
        val fulfilled = costService!!.isCastingCostFulfilled(cardInstance, "THAT_TARGETS_GET", manaPaid)

        // Then
        Assertions.assertThat(fulfilled).isFalse()
    }

    @Test
    fun canAffordReturnsFalseIfNoLandsAvailable() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = createCardInstance(gameStatus, "Axebane Beast")

        // When
        val result = costService!!.canAfford(cardInstance, null, gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun canAffordReturnsTrueIfEnoughMana() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        createCardInstance(gameStatus, "Forest")
        createCardInstance(gameStatus, "Forest")
        createCardInstance(gameStatus, "Forest")
        createCardInstance(gameStatus, "Forest")
        val cardInstance = createCardInstance(gameStatus, "Axebane Beast")

        // When
        val result = costService!!.canAfford(cardInstance, null, gameStatus)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun canAffordReturnsTrueIfCorrectManaDualLands() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        createCardInstance(gameStatus, "Selesnya Guildgate") // GREEN, WHITE
        createCardInstance(gameStatus, "Llanowar Elves") // GREEN
        createCardInstance(gameStatus, "Leyline Prowler") // any color
        val cardInstance = createCardInstance(gameStatus, "Skyknight Legionnaire") // "RED", "WHITE", "COLORLESS"

        // When
        val result = costService!!.canAfford(cardInstance, null, gameStatus)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun canAffordReturnsFalseIfWrongManaDualLands() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        createCardInstance(gameStatus, "Selesnya Guildgate") // GREEN, WHITE
        createCardInstance(gameStatus, "Llanowar Elves") // GREEN
        createCardInstance(gameStatus, "Dimir Guildgate") // BLUE, BLACK
        val cardInstance = createCardInstance(gameStatus, "Skyknight Legionnaire") // "RED", "WHITE", "COLORLESS"

        // When
        val result = costService!!.canAfford(cardInstance, null, gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun cardThatReallyNeedsColorlessManaShouldFailIfNotColorlessManaFalse() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()

        createCardInstance(gameStatus, "Forest")

        val blindingDrone = createCardInstance(gameStatus, "Blinding Drone")
        gameStatus.getPlayer1().getBattlefield().addCard(blindingDrone)

        // When
        val result = costService!!.canAfford(blindingDrone, "THAT_TARGETS_GET", gameStatus)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun cardThatReallyNeedsColorlessManaShouldFailIfNotColorlessManaTrue() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()

        createCardInstance(gameStatus, "Wastes")

        val blindingDrone = createCardInstance(gameStatus, "Blinding Drone")
        gameStatus.getPlayer1().getBattlefield().addCard(blindingDrone)

        // When
        val result = costService!!.canAfford(blindingDrone, "THAT_TARGETS_GET", gameStatus)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    private fun createCardInstance(gameStatus: GameStatus, cardName: String): CardInstance {
        val card = cards!!.get(cardName)
        val cardInstance =
            cardInstanceFactory!!.create(gameStatus, ++cardInstanceId, card, "player-name", "player-name")
        gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance)
        return cardInstance
    }
}
package integration.turn.action.cast

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.cast.InstantSpeedService
import integration.TestUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CastTestConfiguration::class])
class InstantSpeedServiceTest {
    @Autowired
    private val instantSpeedService: InstantSpeedService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun isInstantSpeedReturnsFalseForLand() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Plains"), "player")

        // When
        val result = instantSpeedService!!.isAtInstantSpeed(cardInstance, null)

        // Then
        Assertions.assertThat(result).isFalse()
    }

    @Test
    fun isInstantSpeedReturnsTrueForInstant() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Murder"), "player")

        // When
        val result = instantSpeedService!!.isAtInstantSpeed(cardInstance, null)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun isInstantSpeedReturnsTrueForMetropolisSpriteSelectedPermanentsGet() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Metropolis Sprite"), "player")
        val playedAbility = "SELECTED_PERMANENTS_GET"

        // When
        val result = instantSpeedService!!.isAtInstantSpeed(cardInstance, playedAbility)

        // Then
        Assertions.assertThat(result).isTrue()
    }

    @Test
    fun isInstantSpeedReturnsFalseForEquip() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val cardInstance = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Cobbled Wings"), "player")
        val playedAbility = "EQUIP"

        // When
        val result = instantSpeedService!!.isAtInstantSpeed(cardInstance, playedAbility)

        // Then
        Assertions.assertThat(result).isFalse()
    }
}
package integration.cardinstance

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestUtilsConfiguration::class])
class CardInstanceFactoryTest {
    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val cards: Cards? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Test
    fun shouldCreateACardInstance() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()

        // When
        val cardInstance = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Short Sword"), "player-name")

        // Then
        Assertions.assertThat(cardInstance.getId()).isEqualTo(1)
        Assertions.assertThat(cardInstance.getOwner()).isEqualTo("player-name")
    }

    @Test
    fun shouldCreateTwoDifferentCardInstances() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()

        // When
        val cardInstance1 = cardInstanceFactory!!.create(gameStatus, 1, cards!!.get("Short Sword"), "player-name")
        val cardInstance2 = cardInstanceFactory.create(gameStatus, 2, cards.get("Befuddle"), "opponent-name")

        // Then
        Assertions.assertThat<CardInstance?>(cardInstance1).isNotSameAs(cardInstance2)
    }
}
package integration.cardinstance

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestUtilsConfiguration::class])
class CardInstanceFactoryTest(
    @param:Autowired val cardInstanceFactory: CardInstanceFactory,
    @param:Autowired val cards: Cards,
    @param:Autowired val testUtils: TestUtils
) {
    @Test
    fun shouldCreateACardInstance() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        // When
        val cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Short Sword"), "player-name")

        // Then
        assertThat(cardInstance.id).isEqualTo(1)
        assertThat(cardInstance.owner).isEqualTo("player-name")
    }

    @Test
    fun shouldCreateTwoDifferentCardInstances() {
        // Given
        val gameStatus = testUtils.testGameStatus()

        // When
        val cardInstance1 = cardInstanceFactory.create(gameStatus, 1, cards.get("Short Sword"), "player-name")
        val cardInstance2 = cardInstanceFactory.create(gameStatus, 2, cards.get("Befuddle"), "opponent-name")

        // Then
        assertThat(cardInstance1).isNotSameAs(cardInstance2)
    }
}
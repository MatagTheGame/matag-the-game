package integration.cardinstance

import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.cardinstance.CardInstanceFactory
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class CardInstanceFactoryTest(
    val cardInstanceFactory: CardInstanceFactory,
    val cards: Cards,
    val testUtils: TestUtils
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
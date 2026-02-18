package integration.deck

import com.matag.adminentities.DeckInfo
import com.matag.cards.Cards
import com.matag.game.deck.DeckFactory
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestUtilsConfiguration::class])
class DeckFactoryTest(
    val testUtils: TestUtils,
    val deckFactory: DeckFactory,
    val cards: Cards
) {
    @Test
    fun testFactory() {
        // Given
        val deckInfo = DeckInfo(listOf(cards.get("Plains")))

        // When
        val cards = deckFactory.create("playerName", testUtils.testGameStatus(), deckInfo)

        // Then
        assertThat(cards).hasSize(1)
    }
}
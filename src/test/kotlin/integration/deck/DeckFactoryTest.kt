package integration.deck

import com.matag.adminentities.DeckInfo
import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.deck.DeckFactory
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
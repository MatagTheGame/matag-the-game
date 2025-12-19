package integration.deck

import com.matag.adminentities.DeckInfo
import com.matag.cards.Card
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.deck.DeckFactory
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForInterfaceTypes
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.List

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestUtilsConfiguration::class])
class DeckFactoryTest(
    @param:Autowired val testUtils: TestUtils,
    @param:Autowired val deckFactory: DeckFactory,
    @param:Autowired val cards: Cards
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
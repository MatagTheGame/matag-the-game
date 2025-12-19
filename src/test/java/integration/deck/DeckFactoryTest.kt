package integration.deck

import com.matag.adminentities.DeckInfo
import com.matag.cards.Card
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.deck.DeckFactory
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.AssertionsForInterfaceTypes
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.List

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestUtilsConfiguration::class])
class DeckFactoryTest {
    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val deckFactory: DeckFactory? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun testFactory() {
        // Given
        val deckInfo = DeckInfo(List.of<Card?>(cards!!.get("Plains")))

        // When
        val cards = deckFactory!!.create("playerName", testUtils!!.testGameStatus(), deckInfo)

        // Then
        AssertionsForInterfaceTypes.assertThat<CardInstance?>(cards).hasSize(1)
    }
}
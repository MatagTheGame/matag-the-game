package integration

import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.player.PlayerFactory
import com.matag.game.stack.SpellStack
import com.matag.game.status.GameStatusFactory
import com.matag.game.turn.Turn
import integration.cardinstance.CardsTestConfiguration
import integration.deck.DeckTestConfiguration
import integration.player.PlayerTestConfiguration
import integration.status.GameStatusTestConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    CardsTestConfiguration::class,
    GameStatusTestConfiguration::class,
    PlayerTestConfiguration::class,
    Turn::class,
    SpellStack::class,
    DeckTestConfiguration::class
)
open class TestUtilsConfiguration {
    @Bean
    open fun testUtils(
        gameStatusFactory: GameStatusFactory,
        playerFactory: PlayerFactory,
        cardInstanceFactory: CardInstanceFactory,
        cards: Cards
    ): TestUtils {
        return TestUtils(gameStatusFactory, playerFactory, cardInstanceFactory, cards)
    }

    @Bean
    open fun adminClient(): AdminClient? {
        return Mockito.mock<AdminClient?>(AdminClient::class.java)
    }
}
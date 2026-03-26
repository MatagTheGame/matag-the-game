package integration

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.init.NoInitService
import com.matag.game.player.PlayerFactory
import com.matag.game.status.GameStatusFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
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
    open fun noInitSevice() = NoInitService()
}
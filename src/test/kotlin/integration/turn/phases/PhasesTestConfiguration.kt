package integration.turn.phases

import com.matag.game.turn.action._continue.AutocontinueChecker
import com.matag.game.turn.action._continue.ConsolidateStatusService
import com.matag.game.turn.action.combat.CombatService
import com.matag.game.turn.action.player.DiscardXCardsService
import com.matag.game.turn.action.player.DrawXCardsService
import integration.TestUtilsConfiguration
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.bean.override.mockito.MockitoBean

@Configuration
@ComponentScan("com.matag.game.turn.phases")
@Import(TestUtilsConfiguration::class)
open class PhasesTestConfiguration {
    @Bean
    open fun drawXCardsService() : DrawXCardsService = mock()

    @Bean
    open fun discardXCardsService() : DiscardXCardsService = mock()

    @Bean
    open fun autocontinueChecker() : AutocontinueChecker = mock()

    @Bean
    open fun combatService() : CombatService = mock()

    @Bean
    open fun consolidateStatusService() : ConsolidateStatusService = mock()
}
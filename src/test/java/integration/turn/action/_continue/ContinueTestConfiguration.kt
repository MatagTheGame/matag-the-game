package integration.turn.action._continue

import com.matag.game.turn.action.AbilityActionFactory
import com.matag.game.turn.action.cast.InstantSpeedService
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import com.matag.game.turn.action.player.DiscardXCardsService
import com.matag.game.turn.action.player.ScryXCardsService
import com.matag.game.turn.action.target.TargetCheckerService
import com.matag.game.turn.phases.PhaseFactory
import integration.TestUtilsConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.*

@Configuration
@ComponentScan("com.matag.game.turn.action._continue")
@Import(TestUtilsConfiguration::class)
open class ContinueTestConfiguration {
    @Bean
    open fun instantSpeedService(): InstantSpeedService {
        return InstantSpeedService()
    }

    @Bean
    @Primary
    open fun phaseFactory(): PhaseFactory? {
        return Mockito.mock<PhaseFactory?>(PhaseFactory::class.java)
    }

    @Bean
    @Primary
    open fun abilityActionFactory(): AbilityActionFactory? {
        return Mockito.mock<AbilityActionFactory?>(AbilityActionFactory::class.java)
    }

    @Bean
    @Primary
    open fun enterCardIntoBattlefieldService(): EnterCardIntoBattlefieldService? {
        return Mockito.mock<EnterCardIntoBattlefieldService?>(EnterCardIntoBattlefieldService::class.java)
    }

    @Bean
    @Primary
    open fun targetCheckerService(): TargetCheckerService? {
        return Mockito.mock<TargetCheckerService?>(TargetCheckerService::class.java)
    }

    @Bean
    @Primary
    open fun discardXCardsService(): DiscardXCardsService? {
        return Mockito.mock<DiscardXCardsService?>(DiscardXCardsService::class.java)
    }

    @Bean
    @Primary
    open fun scryXCardsService(): ScryXCardsService? {
        return Mockito.mock<ScryXCardsService?>(ScryXCardsService::class.java)
    }
}
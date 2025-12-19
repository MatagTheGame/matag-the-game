package integration.turn.action.cast

import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import com.matag.game.turn.action.tap.TapPermanentService
import com.matag.game.turn.action.target.TargetCheckerService
import com.matag.game.turn.action.trigger.WhenTriggerService
import integration.TestUtilsConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan("com.matag.game.turn.action.cast")
@Import(TestUtilsConfiguration::class)
open class CastTestConfiguration {
    @Bean
    open fun targetCheckerService(): TargetCheckerService? {
        return Mockito.mock<TargetCheckerService?>(TargetCheckerService::class.java)
    }

    @Bean
    open fun enterCardIntoBattlefieldService(): EnterCardIntoBattlefieldService? {
        return Mockito.mock<EnterCardIntoBattlefieldService?>(EnterCardIntoBattlefieldService::class.java)
    }

    @Bean
    open fun tapPermanentService(): TapPermanentService? {
        return Mockito.mock<TapPermanentService?>(TapPermanentService::class.java)
    }

    @Bean
    open fun whenTriggerService(): WhenTriggerService? {
        return Mockito.mock<WhenTriggerService?>(WhenTriggerService::class.java)
    }
}
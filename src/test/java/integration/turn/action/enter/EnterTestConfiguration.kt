package integration.turn.action.enter

import com.matag.game.turn.action.cast.ManaCountService
import com.matag.game.turn.action.player.DrawXCardsService
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService
import com.matag.game.turn.action.trigger.WhenTriggerService
import integration.TestUtilsConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan("com.matag.game.turn.action.enter")
@Import(TestUtilsConfiguration::class)
open class EnterTestConfiguration {
    @Bean
    open fun manaCountService(): ManaCountService {
        return ManaCountService()
    }

    @Bean
    open fun drawXCardsService(): DrawXCardsService? {
        return Mockito.mock<DrawXCardsService?>(DrawXCardsService::class.java)
    }

    @Bean
    open fun magicInstanceSelectorService(): MagicInstancePermanentSelectorService {
        return MagicInstancePermanentSelectorService()
    }

    @Bean
    open fun whenTriggerService(magicInstancePermanentSelectorService: MagicInstancePermanentSelectorService?): WhenTriggerService {
        return WhenTriggerService(magicInstancePermanentSelectorService)
    }
}
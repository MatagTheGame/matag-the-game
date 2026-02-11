package integration.turn.action.target

import com.matag.cards.ability.AbilityService
import com.matag.game.turn.action.damage.DealDamageToPlayerService
import com.matag.game.turn.action.permanent.PermanentGetService
import com.matag.game.turn.action.player.DrawXCardsService
import com.matag.game.turn.action.player.LifeService
import integration.TestUtilsConfiguration
import integration.turn.action.selection.SelectionTestConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.*

@Configuration
@ComponentScan("com.matag.game.turn.action.target")
@Import(TestUtilsConfiguration::class, SelectionTestConfiguration::class)
open class TargetTestConfiguration {
    @Bean
    @Primary
    open fun lifeService(): LifeService? {
        return Mockito.mock<LifeService?>(LifeService::class.java)
    }

    @Bean
    @Primary
    open fun drawXCardsService(): DrawXCardsService? {
        return Mockito.mock<DrawXCardsService?>(DrawXCardsService::class.java)
    }

    @Bean
    @Primary
    open fun permanentService(): PermanentGetService? {
        return Mockito.mock<PermanentGetService?>(PermanentGetService::class.java)
    }

    @Bean
    @Primary
    open fun dealDamageToPlayerService(): DealDamageToPlayerService? {
        return Mockito.mock<DealDamageToPlayerService?>(DealDamageToPlayerService::class.java)
    }

    @Bean
    @Primary
    open fun abilityService(): AbilityService? {
        return Mockito.mock<AbilityService?>(AbilityService::class.java)
    }
}
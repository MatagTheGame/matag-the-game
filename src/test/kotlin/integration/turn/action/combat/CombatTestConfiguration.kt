package integration.turn.action.combat

import com.matag.game.turn.action._continue.ContinueService
import com.matag.game.turn.action.damage.DealDamageToCreatureService
import com.matag.game.turn.action.damage.DealDamageToPlayerService
import com.matag.game.turn.action.player.LifeService
import com.matag.game.turn.action.trigger.WhenTriggerService
import integration.TestUtilsConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.*

@Configuration
@ComponentScan("com.matag.game.turn.action.combat")
@Import(TestUtilsConfiguration::class)
open class CombatTestConfiguration {
    @Bean
    @Primary
    open fun lifeService(): LifeService? {
        return Mockito.mock<LifeService?>(LifeService::class.java)
    }

    @Bean
    @Primary
    open fun dealDamageToCreatureService(): DealDamageToCreatureService? {
        return Mockito.mock<DealDamageToCreatureService?>(DealDamageToCreatureService::class.java)
    }

    @Bean
    @Primary
    open fun dealDamageToPlayerService(): DealDamageToPlayerService? {
        return Mockito.mock<DealDamageToPlayerService?>(DealDamageToPlayerService::class.java)
    }

    @Bean
    @Primary
    open fun continueTurnService(): ContinueService? {
        return Mockito.mock<ContinueService?>(ContinueService::class.java)
    }

    @Bean
    @Primary
    open fun whenTriggerService(): WhenTriggerService? {
        return Mockito.mock<WhenTriggerService?>(WhenTriggerService::class.java)
    }
}
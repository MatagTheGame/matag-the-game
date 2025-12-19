package integration.turn.action.player

import com.matag.cards.ability.AbilityService
import com.matag.game.turn.action.damage.DealDamageToPlayerService
import com.matag.game.turn.action.finish.FinishGameService
import com.matag.game.turn.action.leave.PutIntoGraveyardService
import integration.TestUtilsConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@ComponentScan("com.matag.game.turn.action.player")
@Import(TestUtilsConfiguration::class)
open class PlayerTestConfiguration {
    @Bean
    open fun finishGameService(): FinishGameService? {
        return Mockito.mock<FinishGameService?>(FinishGameService::class.java)
    }

    @Bean
    open fun abilityService(): AbilityService? {
        return Mockito.mock<AbilityService?>(AbilityService::class.java)
    }

    @Bean
    open fun dealDamageToPlayerService(): DealDamageToPlayerService? {
        return Mockito.mock<DealDamageToPlayerService?>(DealDamageToPlayerService::class.java)
    }

    @Bean
    open fun putIntoGraveyardService(): PutIntoGraveyardService? {
        return Mockito.mock<PutIntoGraveyardService?>(PutIntoGraveyardService::class.java)
    }
}

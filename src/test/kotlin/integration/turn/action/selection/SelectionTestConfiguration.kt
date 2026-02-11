package integration.turn.action.selection

import com.matag.game.turn.action.permanent.PermanentGetService
import com.matag.game.turn.action.player.PlayerGetService
import integration.TestUtilsConfiguration
import org.mockito.Mockito
import org.springframework.context.annotation.*

@Configuration
@ComponentScan("com.matag.game.turn.action.selection")
@Import(TestUtilsConfiguration::class)
open class SelectionTestConfiguration {
    @Bean
    @Primary
    open fun permanentService(): PermanentGetService? {
        return Mockito.mock<PermanentGetService?>(PermanentGetService::class.java)
    }

    @Bean
    @Primary
    open fun playerGetService(): PlayerGetService? {
        return Mockito.mock<PlayerGetService?>(PlayerGetService::class.java)
    }
}
package integration.status

import com.matag.game.event.EventSender
import com.matag.game.security.SecurityHelper
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@ComponentScan("com.matag.game.status")
open class GameStatusTestConfiguration {
    @Bean
    @Primary
    open fun securityHelper(): SecurityHelper? {
        return Mockito.mock<SecurityHelper?>(SecurityHelper::class.java)
    }

    @Bean
    @Primary
    open fun eventSender(): EventSender? {
        return Mockito.mock<EventSender?>(EventSender::class.java)
    }
}
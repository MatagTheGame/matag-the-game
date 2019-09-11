package integration.mtg.game.turn.action.cast;

import com.aa.mtg.game.turn.action.enter.EnterCardIntoBattlefieldService;
import com.aa.mtg.game.turn.action.target.TargetCheckerService;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("com.aa.mtg.game.turn.action.cast")
@Import(TestUtilsConfiguration.class)
public class CastTestConfiguration {

    @Bean
    @Primary
    public TargetCheckerService targetCheckerService() {
        return Mockito.mock(TargetCheckerService.class);
    }

    @Bean
    @Primary
    public EnterCardIntoBattlefieldService enterCardIntoBattlefieldService() {
        return Mockito.mock(EnterCardIntoBattlefieldService.class);
    }
}
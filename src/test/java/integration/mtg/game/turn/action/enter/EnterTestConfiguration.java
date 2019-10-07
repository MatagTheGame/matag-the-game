package integration.mtg.game.turn.action.enter;

import com.aa.mtg.game.turn.action.cast.ManaCountService;
import integration.TestUtilsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.aa.mtg.game.turn.action.enter")
@Import(TestUtilsConfiguration.class)
public class EnterTestConfiguration {

    @Bean
    public ManaCountService manaCountService() {
      return new ManaCountService();
    }
}
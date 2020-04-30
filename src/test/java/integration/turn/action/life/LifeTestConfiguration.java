package integration.turn.action.life;

import com.matag.game.turn.action.finish.FinishGameService;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.matag.game.turn.action.life")
@Import(TestUtilsConfiguration.class)
public class LifeTestConfiguration {
  @Bean
  public FinishGameService finishGameService() {
    return Mockito.mock(FinishGameService.class);
  }
}

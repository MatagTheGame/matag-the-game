package integration.turn.action.leave;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.matag.game.turn.action.trigger.WhenTriggerService;

import integration.TestUtilsConfiguration;

@Configuration
@ComponentScan({"com.matag.game.turn.action.leave", "com.matag.game.turn.action.attach"})
@Import(TestUtilsConfiguration.class)
public class LeaveTestConfiguration {

  @Bean
  public WhenTriggerService whenTriggerService() {
    return Mockito.mock(WhenTriggerService.class);
  }
}

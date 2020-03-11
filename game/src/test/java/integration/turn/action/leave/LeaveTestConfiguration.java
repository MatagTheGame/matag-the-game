package integration.turn.action.leave;

import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({"com.matag.game.turn.action.leave", "com.matag.game.turn.action.attach"})
@Import(TestUtilsConfiguration.class)
public class LeaveTestConfiguration {

  @Bean
  public CardInstanceSelectorService cardInstanceSelectorService() {
    return Mockito.mock(CardInstanceSelectorService.class);
  }
}

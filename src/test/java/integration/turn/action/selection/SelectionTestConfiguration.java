package integration.turn.action.selection;

import com.matag.game.turn.action.permanent.PermanentService;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.matag.game.turn.action.selection")
@Import(TestUtilsConfiguration.class)
public class SelectionTestConfiguration {

  @Bean
  @Primary
  public PermanentService permanentService() {
    return Mockito.mock(PermanentService.class);
  }
}
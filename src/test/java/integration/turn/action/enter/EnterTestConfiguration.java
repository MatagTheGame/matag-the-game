package integration.turn.action.enter;

import com.matag.game.turn.action.cast.ManaCountService;
import com.matag.game.turn.action.player.DrawXCardsService;
import com.matag.game.turn.action.selection.MagicInstanceSelectorService;
import com.matag.game.turn.action.trigger.WhenTriggerService;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.matag.game.turn.action.enter")
@Import(TestUtilsConfiguration.class)
public class EnterTestConfiguration {

  @Bean
  public ManaCountService manaCountService() {
    return new ManaCountService();
  }

  @Bean
  public DrawXCardsService drawXCardsService() {
    return Mockito.mock(DrawXCardsService.class);
  }

  @Bean
  public MagicInstanceSelectorService magicInstanceSelectorService() {
    return new MagicInstanceSelectorService();
  }

  @Bean
  public WhenTriggerService whenTriggerService(MagicInstanceSelectorService magicInstanceSelectorService) {
    return new WhenTriggerService(magicInstanceSelectorService);
  }
}
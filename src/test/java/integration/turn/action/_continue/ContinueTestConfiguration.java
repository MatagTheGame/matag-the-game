package integration.turn.action._continue;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import com.matag.game.turn.action.AbilityActionFactory;
import com.matag.game.turn.action.cast.InstantSpeedService;
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService;
import com.matag.game.turn.action.player.DiscardXCardsService;
import com.matag.game.turn.action.player.ScryXCardsService;
import com.matag.game.turn.action.target.TargetCheckerService;
import com.matag.game.turn.phases.PhaseFactory;

import integration.TestUtilsConfiguration;

@Configuration
@ComponentScan("com.matag.game.turn.action._continue")
@Import(TestUtilsConfiguration.class)
public class ContinueTestConfiguration {
  @Bean
  public InstantSpeedService instantSpeedService() {
    return new InstantSpeedService();
  }

  @Bean
  @Primary
  public PhaseFactory phaseFactory() {
    return Mockito.mock(PhaseFactory.class);
  }

  @Bean
  @Primary
  public AbilityActionFactory abilityActionFactory() {
    return Mockito.mock(AbilityActionFactory.class);
  }

  @Bean
  @Primary
  public EnterCardIntoBattlefieldService enterCardIntoBattlefieldService() {
    return Mockito.mock(EnterCardIntoBattlefieldService.class);
  }

  @Bean
  @Primary
  public TargetCheckerService targetCheckerService() {
    return Mockito.mock(TargetCheckerService.class);
  }

  @Bean
  @Primary
  public DiscardXCardsService discardXCardsService() {
    return Mockito.mock(DiscardXCardsService.class);
  }

  @Bean
  @Primary
  public ScryXCardsService scryXCardsService() {
    return Mockito.mock(ScryXCardsService.class);
  }
}
package integration.mtg.game.turn.action._continue;

import com.aa.mtg.game.turn.action.AbilityActionFactory;
import com.aa.mtg.game.turn.action.enter.EnterCardIntoBattlefieldService;
import com.aa.mtg.game.turn.action.target.TargetCheckerService;
import com.aa.mtg.game.turn.phases.PhaseFactory;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.aa.mtg.game.turn.action._continue")
@Import(TestUtilsConfiguration.class)
public class ContinueTestConfiguration {
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
}
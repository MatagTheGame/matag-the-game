package integration.turn.action.combat;

import com.matag.game.turn.action._continue.ContinueService;
import com.matag.game.turn.action.damage.DealDamageToCreatureService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.player.LifeService;
import com.matag.game.turn.action.trigger.WhenTriggerService;
import integration.TestUtilsConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.matag.game.turn.action.combat")
@Import(TestUtilsConfiguration.class)
public class CombatTestConfiguration {

  @Bean
  @Primary
  public LifeService lifeService() {
    return Mockito.mock(LifeService.class);
  }

  @Bean
  @Primary
  public DealDamageToCreatureService dealDamageToCreatureService() {
    return Mockito.mock(DealDamageToCreatureService.class);
  }

  @Bean
  @Primary
  public DealDamageToPlayerService dealDamageToPlayerService() {
    return Mockito.mock(DealDamageToPlayerService.class);
  }

  @Bean
  @Primary
  public ContinueService continueTurnService() {
    return Mockito.mock(ContinueService.class);
  }

  @Bean
  @Primary
  public WhenTriggerService whenTriggerService() {
    return Mockito.mock(WhenTriggerService.class);
  }
}
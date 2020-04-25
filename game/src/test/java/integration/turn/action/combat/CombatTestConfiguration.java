package integration.turn.action.combat;

import com.matag.game.turn.action._continue.ContinueTurnService;
import com.matag.game.turn.action.damage.DealDamageToCreatureService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.life.LifeService;
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
  public ContinueTurnService continueTurnService() {
    return Mockito.mock(ContinueTurnService.class);
  }
}
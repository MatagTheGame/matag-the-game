package integration.turn.action.target;

import com.matag.cards.ability.AbilityService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.draw.DrawXCardsService;
import com.matag.game.turn.action.life.LifeService;
import com.matag.game.turn.action.permanent.PermanentGetService;
import integration.TestUtilsConfiguration;
import integration.turn.action.selection.SelectionTestConfiguration;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.matag.game.turn.action.target")
@Import({TestUtilsConfiguration.class, SelectionTestConfiguration.class})
public class TargetTestConfiguration {

  @Bean
  @Primary
  public LifeService lifeService() {
    return Mockito.mock(LifeService.class);
  }

  @Bean
  @Primary
  public DrawXCardsService drawXCardsService() {
    return Mockito.mock(DrawXCardsService.class);
  }

  @Bean
  @Primary
  public PermanentGetService permanentService() {
    return Mockito.mock(PermanentGetService.class);
  }

  @Bean
  @Primary
  public DealDamageToPlayerService dealDamageToPlayerService() {
    return Mockito.mock(DealDamageToPlayerService.class);
  }

  @Bean
  @Primary
  public AbilityService abilityService() {
    return Mockito.mock(AbilityService.class);
  }
}
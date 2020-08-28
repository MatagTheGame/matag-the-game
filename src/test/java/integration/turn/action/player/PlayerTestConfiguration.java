package integration.turn.action.player;

import com.matag.cards.ability.AbilityService;
import com.matag.game.turn.action.damage.DealDamageToPlayerService;
import com.matag.game.turn.action.finish.FinishGameService;
import com.matag.game.turn.action.leave.PutIntoGraveyardService;
import integration.TestUtilsConfiguration;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.matag.game.turn.action.player")
@Import({TestUtilsConfiguration.class})
public class PlayerTestConfiguration {
  @Bean
  public FinishGameService finishGameService() {
    return Mockito.mock(FinishGameService.class);
  }

  @Bean
  public AbilityService abilityService() {
    return Mockito.mock(AbilityService.class);
  }

  @Bean
  public DealDamageToPlayerService dealDamageToPlayerService() {
    return Mockito.mock(DealDamageToPlayerService.class);
  }

  @Bean PutIntoGraveyardService putIntoGraveyardService() {
    return Mockito.mock(PutIntoGraveyardService.class);
  }
}

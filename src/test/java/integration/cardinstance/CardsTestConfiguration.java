package integration.cardinstance;

import com.matag.cards.CardsConfiguration;
import com.matag.game.cardinstance.CardInstanceConfiguration;
import com.matag.game.turn.action.tap.TapPermanentService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CardsConfiguration.class, CardInstanceConfiguration.class})
public class CardsTestConfiguration {
  @Bean
  public TapPermanentService tapPermanentService() {
    return Mockito.mock(TapPermanentService.class);
  }
}
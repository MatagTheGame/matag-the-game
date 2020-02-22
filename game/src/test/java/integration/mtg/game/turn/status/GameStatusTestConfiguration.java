package integration.mtg.game.turn.status;

import com.aa.mtg.game.event.EventSender;
import com.aa.mtg.game.security.SecurityHelper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("com.aa.mtg.game.status")
public class GameStatusTestConfiguration {

  @Bean
  @Primary
  public SecurityHelper securityHelper() {
    return Mockito.mock(SecurityHelper.class);
  }

  @Bean
  @Primary
  public EventSender eventSender() {
    return Mockito.mock(EventSender.class);
  }

}
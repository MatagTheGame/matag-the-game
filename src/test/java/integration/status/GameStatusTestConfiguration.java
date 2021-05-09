package integration.status;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.matag.game.event.EventSender;
import com.matag.game.security.SecurityHelper;

@Configuration
@ComponentScan("com.matag.game.status")
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
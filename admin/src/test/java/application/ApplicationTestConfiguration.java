package application;

import com.aa.mtg.admin.session.MtgSessionRepository;
import com.aa.mtg.admin.user.MtgUserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableAutoConfiguration(exclude= DataSourceAutoConfiguration.class)
public class ApplicationTestConfiguration {
  @Bean
  @Primary
  public MtgUserRepository mtgUserRepository() {
    return Mockito.mock(MtgUserRepository.class);
  }

  @Bean
  @Primary
  public MtgSessionRepository mtgSessionRepository() {
    return Mockito.mock(MtgSessionRepository.class);
  }
}

package application;

import com.aa.mtg.admin.user.UserRepository;
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
  public UserRepository userRepository() {
    return Mockito.mock(UserRepository.class);
  }
}

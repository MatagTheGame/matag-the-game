package application;

import application.inmemoryrepositories.MtgSessionInMemoryRepository;
import application.inmemoryrepositories.MtgUserInMemoryRepository;
import com.aa.mtg.admin.session.MtgSessionRepository;
import com.aa.mtg.admin.user.MtgUserRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static application.TestUtils.inactive;
import static application.TestUtils.user1;

public abstract class AbstractApplicationTest {

  @Autowired
  private MtgUserRepository mtgUserRepository;

  @Autowired
  private MtgSessionRepository mtgSessionRepository;

  @Before
  public void setupDatabase() {
    mtgUserRepository.save(user1());
    mtgUserRepository.save(inactive());
  }

  @After
  public void cleanupDatabase() {
    mtgUserRepository.deleteAll();
    mtgSessionRepository.deleteAll();
  }

  @Configuration
  @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
  public static class ApplicationTestConfiguration {
    @Bean
    public MtgUserRepository mtgUserRepository() {
      return new MtgUserInMemoryRepository();
    }

    @Bean
    public MtgSessionRepository mtgSessionRepository() {
      return new MtgSessionInMemoryRepository();
    }
  }
}

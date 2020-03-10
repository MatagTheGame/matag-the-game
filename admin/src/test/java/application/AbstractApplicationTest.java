package application;

import application.inmemoryrepositories.MtgSessionInMemoryRepository;
import application.inmemoryrepositories.MtgUserInMemoryRepository;
import com.aa.mtg.admin.session.MtgSession;
import com.aa.mtg.admin.session.MtgSessionRepository;
import com.aa.mtg.admin.user.MtgUserRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static application.TestUtils.inactive;
import static application.TestUtils.user1;
import static com.aa.mtg.admin.session.AuthSessionFilter.SESSION_DURATION_TIME;
import static com.aa.mtg.admin.session.AuthSessionFilter.SESSION_NAME;

public abstract class AbstractApplicationTest {
  public static final String USER_1_SESSION_TOKEN = "00000000-0000-0000-0000-000000000000";

  @Autowired
  private MtgUserRepository mtgUserRepository;

  @Autowired
  private MtgSessionRepository mtgSessionRepository;

  @Autowired
  private Clock clock;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Before
  public void setupDatabase() {
    testRestTemplate.getRestTemplate().getInterceptors().clear();

    mtgUserRepository.save(user1());
    mtgUserRepository.save(inactive());

    mtgSessionRepository.save(MtgSession.builder()
      .id(UUID.fromString(USER_1_SESSION_TOKEN).toString())
      .mtgUser(user1())
      .createdAt(LocalDateTime.now(clock))
      .validUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME))
      .build());
  }

  @After
  public void cleanupDatabase() {
    mtgUserRepository.deleteAll();
    mtgSessionRepository.deleteAll();
  }

  public void user1IsLoggedIn() {
    testRestTemplate.getRestTemplate().setInterceptors(
      Collections.singletonList((request, body, execution) -> {
        request.getHeaders().add(SESSION_NAME, USER_1_SESSION_TOKEN);
        return execution.execute(request, body);
      }));
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

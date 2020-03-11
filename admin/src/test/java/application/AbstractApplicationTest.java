package application;

import application.inmemoryrepositories.MatagSessionInMemoryRepository;
import application.inmemoryrepositories.MatagUserInMemoryRepository;
import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUserRepository;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static application.TestUtils.inactive;
import static application.TestUtils.user1;
import static com.matag.admin.session.AuthSessionFilter.SESSION_DURATION_TIME;
import static com.matag.admin.session.AuthSessionFilter.SESSION_NAME;

public abstract class AbstractApplicationTest {
  public static final String USER_1_SESSION_TOKEN = "00000000-0000-0000-0000-000000000000";

  @Autowired
  private MatagUserRepository matagUserRepository;

  @Autowired
  private MatagSessionRepository matagSessionRepository;

  @Autowired
  private Clock clock;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Before
  public void setupDatabase() {
    setCurrentTime(Instant.parse("2020-01-01T00:00:00Z"));

    testRestTemplate.getRestTemplate().getInterceptors().clear();

    matagUserRepository.save(user1());
    matagUserRepository.save(inactive());

    matagSessionRepository.save(MatagSession.builder()
      .id(UUID.fromString(USER_1_SESSION_TOKEN).toString())
      .matagUser(user1())
      .createdAt(LocalDateTime.now(clock))
      .validUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME))
      .build());
  }

  @After
  public void cleanupDatabase() {
    matagUserRepository.deleteAll();
    matagSessionRepository.deleteAll();
  }

  public void setCurrentTime(Instant currentTime) {
    ((MockClock) clock).setCurrentTime(currentTime);
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
    public MatagUserRepository matagUserRepository() {
      return new MatagUserInMemoryRepository();
    }

    @Bean
    public MatagSessionRepository matagSessionRepository() {
      return new MatagSessionInMemoryRepository();
    }

    @Bean
    @Primary
    public Clock clock() {
      return new MockClock();
    }
  }
}

package application.session;

import application.AbstractApplicationTest;
import com.aa.mtg.admin.MtgAdminApplication;
import com.aa.mtg.admin.session.MtgSessionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(AbstractApplicationTest.ApplicationTestConfiguration.class)
@ActiveProfiles("test")
public class AuthSessionFilterTest extends AbstractApplicationTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MtgSessionRepository mtgSessionRepository;

  @Test
  public void shouldGrantAccessToAResourceToLoggedInUsers() {
    // Given
    user1IsLoggedIn();

    // When
    ResponseEntity<String> response = restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void shouldNotGrantAccessToAResourceToNonLoggedInUsers() {
    // When
    ResponseEntity<String> response = restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldNotGrantAccessToAResourceIfUserSessionIsExpired() {
    // Given
    user1IsLoggedIn();
    setCurrentTime(Instant.parse("2020-01-01T01:01:00Z"));

    // When
    ResponseEntity<String> response = restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldExtendTheSessionAfterHalfOfItsLife() {
    // Given
    user1IsLoggedIn();
    setCurrentTime(Instant.parse("2020-01-01T00:45:00Z"));

    // When
    restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(mtgSessionRepository.findById(USER_1_SESSION_TOKEN).isPresent()).isTrue();
    assertThat(mtgSessionRepository.findById(USER_1_SESSION_TOKEN).get().getValidUntil()).isEqualTo(LocalDateTime.parse("2020-01-01T01:45:00"));
  }
}
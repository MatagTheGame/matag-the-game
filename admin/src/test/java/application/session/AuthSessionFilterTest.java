package application.session;

import application.AbstractApplicationTest;
import com.matag.admin.MatagAdminApplication;
import com.matag.admin.session.MatagSessionRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(AbstractApplicationTest.ApplicationTestConfiguration.class)
@ActiveProfiles("test")
public class AuthSessionFilterTest extends AbstractApplicationTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MatagSessionRepository matagSessionRepository;

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
    setCurrentTime(TEST_START_TIME.plusHours(1).plusMinutes(1));

    // When
    ResponseEntity<String> response = restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldExtendTheSessionAfterHalfOfItsLife() {
    // Given
    user1IsLoggedIn();
    setCurrentTime(TEST_START_TIME.plusMinutes(45));

    // When
    restTemplate.getForEntity("/path/to/a/resource", String.class);

    // Then
    assertThat(matagSessionRepository.findById(USER_1_SESSION_TOKEN).isPresent()).isTrue();
    assertThat(matagSessionRepository.findById(USER_1_SESSION_TOKEN).get().getValidUntil()).isEqualTo(TEST_START_TIME.plusHours(1).plusMinutes(45));
  }
}
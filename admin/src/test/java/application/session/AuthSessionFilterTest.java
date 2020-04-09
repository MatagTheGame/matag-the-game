package application.session;

import application.AbstractApplicationTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class AuthSessionFilterTest extends AbstractApplicationTest {
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
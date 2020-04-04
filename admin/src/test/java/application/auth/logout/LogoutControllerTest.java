package application.auth.logout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import application.AbstractApplicationTest;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class LogoutControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldLogoutAUser() {
    // Given
    matagSessionRepository.deleteAll();
    user1IsLoggedIn();

    // When
    ResponseEntity<String> logoutResponse = restTemplate.getForEntity("/auth/logout", String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
    assertThat(matagSessionRepository.count()).isEqualTo(0);
  }

  @Test
  public void shouldLogoutANonLoggedInUser() {
    // When
    ResponseEntity<String> logoutResponse = restTemplate.getForEntity("/auth/logout", String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
  }
}
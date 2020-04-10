package application.auth.logout;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import application.AbstractApplicationTest;
import com.matag.admin.session.MatagSessionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class LogoutControllerTest extends AbstractApplicationTest {
  @Autowired
  private MatagSessionRepository matagSessionRepository;

  @Test
  public void shouldLogoutAUser() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN);

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
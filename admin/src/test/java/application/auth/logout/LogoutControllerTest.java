package application.auth.logout;

import application.AbstractApplicationTest;
import com.aa.mtg.admin.MtgAdminApplication;
import com.aa.mtg.admin.session.MtgSessionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(AbstractApplicationTest.ApplicationTestConfiguration.class)
@ActiveProfiles("test")
public class LogoutControllerTest extends AbstractApplicationTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MtgSessionRepository mtgSessionRepository;

  @Test
  public void shouldLogoutAUser() {
    // Given
    mtgSessionRepository.deleteAll();
    user1IsLoggedIn();

    // When
    ResponseEntity<String> logoutResponse = restTemplate.getForEntity("/auth/logout", String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
    assertThat(mtgSessionRepository.count()).isEqualTo(0);
  }

  @Test
  public void shouldLogoutANonLoggedInUser() {
    // When
    ResponseEntity<String> logoutResponse = restTemplate.getForEntity("/auth/logout", String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
  }
}
package application.auth.logout;

import application.AbstractApplicationTest;
import com.aa.mtg.admin.MtgAdminApplication;
import com.aa.mtg.admin.auth.login.LoginRequest;
import com.aa.mtg.admin.auth.login.LoginResponse;
import com.aa.mtg.admin.session.MtgSessionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static com.aa.mtg.admin.session.AuthSessionFilter.SESSION_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(AbstractApplicationTest.ApplicationTestConfiguration.class)
public class LogoutControllerTest extends AbstractApplicationTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MtgSessionRepository mtgSessionRepository;

  @Test
  public void shouldLogoutAUser() {
    // Given
    LoginRequest loginRequest = new LoginRequest("user1", "password");
    LoginResponse loginResponse = restTemplate.postForObject("/auth/login", loginRequest, LoginResponse.class);

    // When
    HttpEntity<String> httpEntity = request(loginResponse.getToken());
    ResponseEntity<String> logoutResponse = restTemplate.exchange("/auth/logout", GET, httpEntity, String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
    assertThat(mtgSessionRepository.count()).isEqualTo(0);
  }

  @Test
  public void shouldLogoutANonLoggedInUser() {
    // When
    HttpEntity<String> httpEntity = request("invalid-token");
    ResponseEntity<String> logoutResponse = restTemplate.exchange("/auth/logout", GET, httpEntity, String.class);

    // Then
    assertThat(logoutResponse.getStatusCode()).isEqualTo(OK);
  }

  private static HttpEntity<String> request(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(SESSION_NAME, token);
    return new HttpEntity<>("", headers);
  }
}
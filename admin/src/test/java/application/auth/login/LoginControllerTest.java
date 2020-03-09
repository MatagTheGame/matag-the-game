package application.auth.login;

import application.AbstractApplicationTest;
import com.aa.mtg.admin.MtgAdminApplication;
import com.aa.mtg.admin.auth.login.LoginRequest;
import com.aa.mtg.admin.auth.login.LoginResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(AbstractApplicationTest.ApplicationTestConfiguration.class)
public class LoginControllerTest extends AbstractApplicationTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldLoginAUser() {
    // Given
    LoginRequest request = new LoginRequest("user1", "password");

    // When
    LoginResponse response = restTemplate.postForObject("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getToken()).isNotBlank();
  }

  @Test
  public void shouldNotLoginWithWrongPassword() {
    // Given
    LoginRequest request = new LoginRequest("user1", "wrong-password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
  }

  @Test
  public void shouldNotLoginNotActiveUser() {
    // Given
    LoginRequest request = new LoginRequest("inactive", "password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
  }
}
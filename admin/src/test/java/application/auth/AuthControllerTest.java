package application.auth;

import application.ApplicationTestConfiguration;
import com.aa.mtg.admin.MtgAdminApplication;
import com.aa.mtg.admin.auth.LoginRequest;
import com.aa.mtg.admin.auth.LoginResponse;
import com.aa.mtg.admin.user.MtgUserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static application.TestUtils.inactive;
import static application.TestUtils.user1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(ApplicationTestConfiguration.class)
public class AuthControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private MtgUserRepository userRepository;

  @After
  public void cleanup() {
    Mockito.reset(userRepository);
  }

  @Test
  public void shouldLoginAUser() {
    // Given
    given(userRepository.findByUsername("user1")).willReturn(Optional.of(user1()));
    LoginRequest request = new LoginRequest("user1", "password");

    // When
    LoginResponse response = restTemplate.postForObject("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getToken()).isNotBlank();
  }

  @Test
  public void shouldNotLoginWithWrongPassword() {
    // Given
    given(userRepository.findByUsername("user1")).willReturn(Optional.of(user1()));
    LoginRequest request = new LoginRequest("user1", "wrong-password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
  }

  @Test
  public void shouldNotLoginNotActiveUser() {
    // Given
    given(userRepository.findByUsername("inactive")).willReturn(Optional.of(inactive()));
    LoginRequest request = new LoginRequest("inactive", "password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
  }
}
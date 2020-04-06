package application.auth.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import application.AbstractApplicationTest;
import com.matag.admin.auth.login.LoginRequest;
import com.matag.admin.auth.login.LoginResponse;
import com.matag.admin.user.profile.CurrentUserProfileDto;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

public class LoginControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldReturnInvalidEmail() {
    // Given
    LoginRequest request = new LoginRequest("invalidEmail", "password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Email is invalid.");
  }

  @Test
  public void shouldReturnInvalidPassword() {
    // Given
    LoginRequest request = new LoginRequest("user1@matag.com", "xxx");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Password is invalid.");
  }

  @Test
  public void shouldLoginAUser() {
    // Given
    LoginRequest request = new LoginRequest("user1@matag.com", "password");

    // When
    LoginResponse response = restTemplate.postForObject("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getToken()).isNotBlank();
    assertThat(response.getProfile()).isEqualTo(CurrentUserProfileDto.builder()
      .username("User1")
      .type("USER")
      .build());
  }

  @Test
  public void shouldNotLoginANonExistingUser() {
    // Given
    LoginRequest request = new LoginRequest("non-existing-user@matag.com", "password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Email or password are not correct.");
  }

  @Test
  public void shouldNotLoginWithWrongPassword() {
    // Given
    LoginRequest request = new LoginRequest("user1@matag.com", "wrong-password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Email or password are not correct.");
  }

  @Test
  public void shouldNotLoginNotActiveUser() {
    // Given
    LoginRequest request = new LoginRequest("inactive@matag.com", "password");

    // When
    ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getError()).isEqualTo("Account is not active.");
  }
}
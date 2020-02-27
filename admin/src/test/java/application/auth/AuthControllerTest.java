package application.auth;

import application.ApplicationTestConfiguration;
import com.aa.mtg.admin.AdminApplication;
import com.aa.mtg.admin.auth.LoginRequest;
import com.aa.mtg.admin.auth.LoginResponse;
import com.aa.mtg.admin.user.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.TestUtils.user1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(ApplicationTestConfiguration.class)
public class AuthControllerTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;

  @After
  public void cleanup() {
    Mockito.reset(userRepository);
  }

  @Test
  public void shouldLoginAUser() {
    // Given
    given(userRepository.findByUsername("user1")).willReturn(user1());
    LoginRequest request = new LoginRequest("user1", "bbb");

    // When
    LoginResponse response = restTemplate.postForObject("/auth/login", request, LoginResponse.class);

    // Then
    assertThat(response.getToken()).isEqualTo("password1");
  }
}
package application.stats;

import application.AbstractApplicationTest;
import com.matag.admin.MatagAdminApplication;
import com.matag.admin.stats.StatsResponse;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagAdminApplication.class, webEnvironment = RANDOM_PORT)
@Import(AbstractApplicationTest.ApplicationTestConfiguration.class)
@ActiveProfiles("test")
public class StatsControllerTest extends AbstractApplicationTest {
  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void shouldGetStatsAsUnauthenticatedUser() {
    // When
    ResponseEntity<StatsResponse> response = restTemplate.getForEntity("/stats", StatsResponse.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void shouldGetTotalUsers() {
    // When
    StatsResponse response = restTemplate.getForObject("/stats", StatsResponse.class);

    // Then
    assertThat(response.getTotalUsers()).isEqualTo(2);
  }

  @Test
  public void shouldGetOnlineUsers() {
    // Given
    setCurrentTime(LocalDateTime.parse("2000-01-01T00:00:00"));
    loginUser(USER_2_SESSION_TOKEN);
    setCurrentTime(TEST_START_TIME);

    // When
    StatsResponse response = restTemplate.getForObject("/stats", StatsResponse.class);

    // Then
    assertThat(response.getOnlineUsers()).isEqualTo(1);
  }

  @Test
  public void shouldGetNumOfCards() {
    // When
    StatsResponse response = restTemplate.getForObject("/stats", StatsResponse.class);

    // Then
    assertThat(response.getTotalCards()).isGreaterThan(100);
  }
}
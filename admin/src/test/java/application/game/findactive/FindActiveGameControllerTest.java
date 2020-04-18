package application.game.findactive;

import application.AbstractApplicationTest;
import com.matag.admin.game.findactive.ActiveGameResponse;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import org.junit.Test;

import static application.TestUtils.user1;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;

public class FindActiveGameControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldFindNoGames() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());

    // When
    ActiveGameResponse response = restTemplate.getForObject("/game", ActiveGameResponse.class);

    // Then
    assertThat(response.getGameId()).isNull();
  }

  @Test
  public void shouldFindAnActiveGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());

    JoinGameRequest request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();
    restTemplate.postForObject("/game", request, JoinGameResponse.class);

    // When
    ActiveGameResponse response = restTemplate.getForObject("/game", ActiveGameResponse.class);

    // Then
    assertThat(response.getGameId()).isGreaterThan(0);
    assertThat(response.getCreatedAt()).isNotNull();
    assertThat(response.getPlayerName()).isEqualTo("User1");
    assertThat(response.getPlayerOptions()).isEqualTo("player1 options");
    assertThat(response.getOpponentName()).isNull();
    assertThat(response.getOpponentOptions()).isNull();
  }
}
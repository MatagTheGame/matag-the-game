package application.game.finish;

import application.AbstractApplicationTest;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.adminentities.FinishGameRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static application.TestUtils.user1;
import static application.TestUtils.user2;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

public class FinishGameControllerTest extends AbstractApplicationTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Test
  public void requiresAdminAuthentication() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());

    // When
    FinishGameRequest finishGameRequest = new FinishGameRequest(USER_1_SESSION_TOKEN);
    ResponseEntity<Object> response = restTemplate.postForEntity("/game/1/finish", finishGameRequest, Object.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
  }

  @Test
  public void shouldFinishAGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());
    JoinGameRequest request1 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();
    JoinGameResponse joinGameResponse = restTemplate.postForObject("/game", request1, JoinGameResponse.class);
    Long gameId = joinGameResponse.getGameId();

    userIsLoggedIn(USER_2_SESSION_TOKEN, user2());
    JoinGameRequest request2 = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player2 options")
      .build();
    restTemplate.postForObject("/game", request2, JoinGameResponse.class);

    // When
    adminAuthentication();
    FinishGameRequest finishGameRequest = new FinishGameRequest(USER_1_SESSION_TOKEN);
    ResponseEntity<Object> response = restTemplate.postForEntity("/game/" + gameId + "/finish", finishGameRequest, Object.class);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(OK);
  }
}
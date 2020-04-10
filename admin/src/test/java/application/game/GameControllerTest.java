package application.game;

import application.AbstractApplicationTest;
import com.matag.admin.game.Game;
import com.matag.admin.game.GameRepository;
import com.matag.admin.game.JoinGameRequest;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static application.TestUtils.user1;
import static com.matag.admin.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest extends AbstractApplicationTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Test
  public void shouldCreateAGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN);
    JoinGameRequest request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    // When
    Long response = restTemplate.postForObject("/game", request, Long.class);

    // Then
    assertThat(response).isEqualTo(1);
    Optional<Game> game = gameRepository.findById(1L);
    assertThat(game).isNotEmpty();
    assertThat(game.get().getType()).isEqualTo(UNLIMITED);

    Optional<GameSession> gameSession = gameSessionRepository.findById(1L);
    assertThat(gameSession).isNotEmpty();
    assertThat(gameSession.get().getGame()).isEqualTo(game.get());
    assertThat(gameSession.get().getSessionNum()).isEqualTo(1);
    assertThat(gameSession.get().getSession().getId()).isEqualTo(USER_1_SESSION_TOKEN);
    assertThat(gameSession.get().getPlayer()).isEqualTo(user1());
    assertThat(gameSession.get().getPlayerOptions()).isEqualTo("player1 options");
  }

  @Test
  public void shouldJoinAnExistingGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN);
    JoinGameRequest player1JoinRequest = JoinGameRequest.builder()
      .playerOptions("player1 options")
      .build();

    restTemplate.postForObject("/game", player1JoinRequest, Long.class);

    userIsLoggedIn(USER_2_SESSION_TOKEN);

    JoinGameRequest player2JoinRequest = JoinGameRequest.builder()
      .playerOptions("player2 options")
      .build();

    // When
    Long response = restTemplate.postForObject("/game", player2JoinRequest, Long.class);

    // Then
    assertThat(response).isEqualTo(1);
  }
}
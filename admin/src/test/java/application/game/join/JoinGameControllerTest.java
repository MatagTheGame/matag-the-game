package application.game.join;

import application.AbstractApplicationTest;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.join.JoinGameRequest;
import com.matag.admin.game.join.JoinGameResponse;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.Optional;

import static application.TestUtils.user1;
import static application.TestUtils.user2;
import static com.matag.admin.game.game.GameStatusType.IN_PROGRESS;
import static com.matag.admin.game.game.GameStatusType.STARTING;
import static com.matag.admin.game.game.GameType.UNLIMITED;
import static org.assertj.core.api.Assertions.assertThat;

public class JoinGameControllerTest extends AbstractApplicationTest {
  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GameSessionRepository gameSessionRepository;

  @Test
  public void shouldCreateAGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());
    JoinGameRequest request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    // When
    JoinGameResponse response = restTemplate.postForObject("/game", request, JoinGameResponse.class);

    // Then
    assertThat(response).isEqualTo(JoinGameResponse.builder().gameId(1L).build());

    Optional<Game> game = gameRepository.findById(1L);
    assertThat(game).isNotEmpty();
    assertThat(game.get().getType()).isEqualTo(UNLIMITED);
    assertThat(game.get().getStatus()).isEqualTo(STARTING);

    Iterable<GameSession> gameSessions = gameSessionRepository.findAll();
    assertThat(gameSessions).hasSize(1);
    GameSession gameSession = gameSessions.iterator().next();
    assertThat(gameSession.getGame()).isEqualTo(game.get());
    assertThat(gameSession.getSession().getId()).isEqualTo(USER_1_SESSION_TOKEN);
    assertThat(gameSession.getPlayer()).isEqualTo(user1());
    assertThat(gameSession.getPlayerOptions()).isEqualTo("player1 options");
  }

  @Test
  public void shouldJoinAnExistingGame() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());
    JoinGameRequest player1JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    restTemplate.postForObject("/game", player1JoinRequest, JoinGameResponse.class);

    userIsLoggedIn(USER_2_SESSION_TOKEN, user2());

    JoinGameRequest player2JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player2 options")
      .build();

    // When
    JoinGameResponse response = restTemplate.postForObject("/game", player2JoinRequest, JoinGameResponse.class);

    // Then
    assertThat(response).isEqualTo(JoinGameResponse.builder().gameId(1L).build());

    Optional<Game> game = gameRepository.findById(1L);
    assertThat(game).isNotEmpty();
    assertThat(game.get().getType()).isEqualTo(UNLIMITED);
    assertThat(game.get().getStatus()).isEqualTo(IN_PROGRESS);

    Iterable<GameSession> gameSessions = gameSessionRepository.findAll();
    assertThat(gameSessions).hasSize(2);
    Iterator<GameSession> iterator = gameSessions.iterator();
    GameSession firstGameSession = iterator.next();
    assertThat(firstGameSession.getGame()).isEqualTo(game.get());
    assertThat(firstGameSession.getSession().getId()).isEqualTo(USER_1_SESSION_TOKEN);
    assertThat(firstGameSession.getPlayer()).isEqualTo(user1());
    assertThat(firstGameSession.getPlayerOptions()).isEqualTo("player1 options");

    GameSession secondGameSession = iterator.next();
    assertThat(secondGameSession.getGame()).isEqualTo(game.get());
    assertThat(secondGameSession.getSession().getId()).isEqualTo(USER_2_SESSION_TOKEN);
    assertThat(secondGameSession.getPlayer()).isEqualTo(user2());
    assertThat(secondGameSession.getPlayerOptions()).isEqualTo("player2 options");
  }

  @Test
  public void userCannotStartAnotherGameIfAlreadyInOne() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());
    JoinGameRequest player1JoinRequest = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("player1 options")
      .build();

    restTemplate.postForObject("/game", player1JoinRequest, JoinGameResponse.class);

    // When
    JoinGameResponse response = restTemplate.postForObject("/game", player1JoinRequest, JoinGameResponse.class);

    // Then
    assertThat(response).isEqualTo(JoinGameResponse.builder()
      .errorMessage("You are already in a game.")
      .activeGameId(1L)
      .build());
  }
}
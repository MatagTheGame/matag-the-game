package application.game;

import application.AbstractApplicationTest;
import com.matag.admin.game.Game;
import com.matag.admin.game.GameRepository;
import com.matag.admin.game.GameType;
import com.matag.admin.game.JoinGameRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest extends AbstractApplicationTest {

  @Autowired
  private GameRepository gameRepository;

  @Test
  public void shouldCreateAGame() {
    // Given
    user1IsLoggedIn();
    JoinGameRequest request = JoinGameRequest.builder()
      .gameType(GameType.UNLIMITED)
      .playerOptions("player1 options")
      .build();

    // When
    Long response = restTemplate.postForObject("/game", request, Long.class);

    // Then
    assertThat(response).isEqualTo(1);
    Optional<Game> game = gameRepository.findById(1L);
    assertThat(game).isNotEmpty();
    assertThat(game.get().getType()).isEqualTo(GameType.UNLIMITED);
  }
}
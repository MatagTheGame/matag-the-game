package application.game.deck;

import application.AbstractApplicationTest;
import com.matag.admin.game.JoinGameRequest;
import com.matag.admin.game.JoinGameResponse;
import com.matag.adminentities.DeckInfo;
import org.junit.Test;

import java.util.Set;

import static application.TestUtils.user1;
import static com.matag.admin.game.GameType.UNLIMITED;
import static com.matag.cards.properties.Color.RED;
import static com.matag.cards.properties.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

public class GameControllerTest extends AbstractApplicationTest {
  @Test
  public void shouldRetrieveGameInfo() {
    // Given
    userIsLoggedIn(USER_1_SESSION_TOKEN, user1());
    JoinGameRequest request = JoinGameRequest.builder()
      .gameType(UNLIMITED)
      .playerOptions("{\"randomColors\": [\"WHITE\", \"RED\"]}}")
      .build();

    restTemplate.postForObject("/game", request, JoinGameResponse.class);

    // When
    DeckInfo deckInfo = restTemplate.getForObject("/game/active-deck", DeckInfo.class);

    // Then
    assertThat(deckInfo.getRandomColors()).isEqualTo(Set.of(WHITE, RED));
  }
}
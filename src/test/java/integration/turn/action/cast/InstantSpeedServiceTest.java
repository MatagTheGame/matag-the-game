package integration.turn.action.cast;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.cast.InstantSpeedService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CastTestConfiguration.class)
public class InstantSpeedServiceTest {
  @Autowired
  private InstantSpeedService instantSpeedService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Test
  public void isInstantSpeedReturnsFalseForLand() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), "player");

    // When
    boolean result = instantSpeedService.isAtInstantSpeed(cardInstance, null);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void isInstantSpeedReturnsTrueForInstant() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Murder"), "player");

    // When
    boolean result = instantSpeedService.isAtInstantSpeed(cardInstance, null);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void isInstantSpeedReturnsTrueForMetropolisSpriteSelectedPermanentsGet() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Metropolis Sprite"), "player");
    String playedAbility = "SELECTED_PERMANENTS_GET";

    // When
    boolean result = instantSpeedService.isAtInstantSpeed(cardInstance, playedAbility);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void isInstantSpeedReturnsFalseForEquip() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Cobbled Wings"), "player");
    String playedAbility = "EQUIP";

    // When
    boolean result = instantSpeedService.isAtInstantSpeed(cardInstance, playedAbility);

    // Then
    assertThat(result).isFalse();
  }
}
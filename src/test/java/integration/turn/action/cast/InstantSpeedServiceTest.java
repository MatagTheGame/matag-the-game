package integration.turn.action.cast;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action.cast.InstantSpeedService;

import integration.TestUtils;

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
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), "player");

    // When
    var result = instantSpeedService.isAtInstantSpeed(cardInstance, null);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void isInstantSpeedReturnsTrueForInstant() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Murder"), "player");

    // When
    var result = instantSpeedService.isAtInstantSpeed(cardInstance, null);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void isInstantSpeedReturnsTrueForMetropolisSpriteSelectedPermanentsGet() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Metropolis Sprite"), "player");
    var playedAbility = "SELECTED_PERMANENTS_GET";

    // When
    var result = instantSpeedService.isAtInstantSpeed(cardInstance, playedAbility);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void isInstantSpeedReturnsFalseForEquip() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Cobbled Wings"), "player");
    var playedAbility = "EQUIP";

    // When
    var result = instantSpeedService.isAtInstantSpeed(cardInstance, playedAbility);

    // Then
    assertThat(result).isFalse();
  }
}
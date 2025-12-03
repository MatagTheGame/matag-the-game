package integration.cardinstance.ability;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestUtilsConfiguration.class)
public class CardInstanceAbilityTest {

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private Cards cards;

  @Test
  public void simpleAbilityText() {
    // Given
    var card = cardInstance("Aven Sentry");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Flying.");
  }

  @Test
  public void enchantedCreatureGetOneAbilityText() {
    // Given
    var card = cardInstance("Knight's Pledge");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Enchanted creature gets +2/+2.");
  }

  @Test
  public void enchantedCreatureGetMultipleAbilitiesText() {
    // Given
    var card = cardInstance("Arcane Flight");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Enchanted creature gets +1/+1 and flying.");
  }

  @Test
  public void selectedPermanentsGetMultipleAbilitiesText() {
    // Given
    var card = cardInstance("Make a Stand");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Creatures you control get +1/+0 and indestructible until end of turn.");
  }

  @Test
  public void itGetsAbilitiesText() {
    // Given
    var card = cardInstance("Brazen Wolves");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Gets +2/+0 until end of turn.");
  }

  @Test
  public void gainXLifeText() {
    // Given
    var card = cardInstance("Highland Game");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("You gain 2 life.");
  }

  @Test
  public void drawXCardsAndLoseXLifeText() {
    // Given
    var card = cardInstance("Tithebearer Giant");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("You draw 1 card and lose 1 life.");
  }

  @Test
  public void eachPlayerGainsXLifeText() {
    // Given
    var card = cardInstance("Centaur Peacemaker");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Each player gain 4 life.");
  }

  @Test
  public void eachOpponentLosesXLifeText() {
    // Given
    var card = cardInstance("Infectious Horror");
    var ability = card.getAbilities().get(0);

    // When
    var text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Each opponent lose 2 life.");
  }

  private CardInstance cardInstance(String cardName) {
    var gameStatus = testUtils.testGameStatus();
    return cardInstanceFactory.create(gameStatus, 1, cards.get(cardName), "player-name");
  }
}
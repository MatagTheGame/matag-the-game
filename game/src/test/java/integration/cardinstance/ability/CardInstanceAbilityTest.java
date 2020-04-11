package integration.cardinstance.ability;

import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import org.junit.Test;

import static com.matag.cards.ability.type.AbilityType.*;
import static com.matag.cards.properties.Type.CREATURE;
import static com.matag.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class CardInstanceAbilityTest {

  @Test
  public void simpleAbilityText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(FLYING);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Flying.");
  }

  @Test
  public void drawXCardsText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(DRAW_X_CARDS, emptyList(), singletonList("2"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Draw 2 cards.");
  }

  @Test
  public void gainXLifeText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(ADD_X_LIFE, emptyList(), singletonList("2"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Gain 2 life.");
  }

  @Test
  public void loseXLifeText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(ADD_X_LIFE, emptyList(), singletonList("-2"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Lose 2 life.");
  }

  @Test
  public void eachPlayerGainsXLifeText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(EACH_PLAYERS_ADD_X_LIFE, emptyList(), singletonList("2"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Each player gains 2 life.");
  }

  @Test
  public void eachPlayerLosesXLifeText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(EACH_PLAYERS_ADD_X_LIFE, emptyList(), singletonList("-2"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Each player loses 2 life.");
  }

  @Test
  public void enchantedCreatureGetOneAbilityText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(ENCHANTED_CREATURE_GETS, emptyList(), singletonList("+2/+2"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Enchanted creature gets +2/+2.");
  }

  @Test
  public void enchantedCreatureGetMultipleAbilitiesText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(ENCHANTED_CREATURE_GETS, emptyList(), asList("+2/+2", "TRAMPLE", "HASTE"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Enchanted creature gets +2/+2, trample and haste.");
  }

  @Test
  public void selectedPermanentsGetMultipleAbilitiesText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(SelectorType.PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build(), asList("+2/+2", "TRAMPLE", "HASTE"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Creatures you control get +2/+2, trample and haste until end of turn.");
  }

  @Test
  public void itGetMultipleAbilitiesText() {
    // Given
    CardInstanceAbility ability = new CardInstanceAbility(SELECTED_PERMANENTS_GET, CardInstanceSelector.builder().selectorType(SelectorType.PERMANENT).itself(true).build(), asList("+2/+2", "TRAMPLE", "HASTE"), null);

    // When
    String text = ability.getAbilityTypeText();

    // Then
    assertThat(text).isEqualTo("Gets +2/+2, trample and haste until end of turn.");
  }
}
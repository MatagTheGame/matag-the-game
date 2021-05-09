package integration.cardinstance.ability;

import static com.matag.cards.ability.type.AbilityType.HASTE;
import static com.matag.cards.ability.type.AbilityType.TRAMPLE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory;

public class CardInstanceAbilityFactoryTest {
  private CardInstanceAbilityFactory cardInstanceAbilityFactory = new CardInstanceAbilityFactory();

  @Test
  public void testAbilitiesFromParameters() {
    // Given
    var parameters = asList("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE");

    // When
    var abilities = cardInstanceAbilityFactory.abilitiesFromParameters(parameters);

    // Then
    assertThat(abilities).isEqualTo(asList(new CardInstanceAbility(TRAMPLE), new CardInstanceAbility(HASTE)));
  }

  @Test
  public void testNoAbilityFromParameter() {
    // Given
    var parameter = "+2/+2";

    // When
    var ability = cardInstanceAbilityFactory.abilityFromParameter(parameter);

    // Then
    assertThat(ability).isEmpty();
  }

  @Test
  public void testTrampleAbilityFromParameter() {
    // Given
    var parameter = "TRAMPLE";

    // When
    var ability = cardInstanceAbilityFactory.abilityFromParameter(parameter);

    // Then
    assertThat(ability).isEqualTo(Optional.of(new CardInstanceAbility(TRAMPLE)));
  }
}
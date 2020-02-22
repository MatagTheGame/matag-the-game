package integration.mtg.cards.ability;

import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cardinstance.ability.CardInstanceAbilityFactory;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.aa.mtg.cards.ability.type.AbilityType.HASTE;
import static com.aa.mtg.cards.ability.type.AbilityType.TRAMPLE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CardInstanceAbilityFactoryTest {
  private CardInstanceAbilityFactory cardInstanceAbilityFactory = new CardInstanceAbilityFactory();

  @Test
  public void testAbilitiesFromParameters() {
    // Given
    List<String> parameters = asList("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE");

    // When
    List<CardInstanceAbility> abilities = cardInstanceAbilityFactory.abilitiesFromParameters(parameters);

    // Then
    assertThat(abilities).isEqualTo(asList(new CardInstanceAbility(TRAMPLE), new CardInstanceAbility(HASTE)));
  }

  @Test
  public void testNoAbilityFromParameter() {
    // Given
    String parameter = "+2/+2";

    // When
    Optional<CardInstanceAbility> ability = cardInstanceAbilityFactory.abilityFromParameter(parameter);

    // Then
    assertThat(ability).isEmpty();
  }

  @Test
  public void testTrampleAbilityFromParameter() {
    // Given
    String parameter = "TRAMPLE";

    // When
    Optional<CardInstanceAbility> ability = cardInstanceAbilityFactory.abilityFromParameter(parameter);

    // Then
    assertThat(ability).isEqualTo(Optional.of(new CardInstanceAbility(TRAMPLE)));
  }
}
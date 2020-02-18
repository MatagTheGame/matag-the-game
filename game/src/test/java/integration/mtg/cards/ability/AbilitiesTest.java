package integration.mtg.cards.ability;

import com.aa.mtg.cards.ability.Ability;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AbilitiesTest {

    @Test
    public void testAbilitiesFromParameters() {
        // Given
        List<String> parameters = asList("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE");

        // When
        List<Ability> abilities = abilitiesFromParameters(parameters);

        // Then
        assertThat(abilities).isEqualTo(asList(TRAMPLE, HASTE));
    }

    @Test
    public void testNoAbilityFromParameter() {
        // Given
        String parameter = "+2/+2";

        // When
        Optional<Ability> ability = abilityFromParameter(parameter);

        // Then
        assertThat(ability).isEmpty();
    }

    @Test
    public void testTrampleAbilityFromParameter() {
        // Given
        String parameter = "TRAMPLE";

        // When
        Optional<Ability> ability = abilityFromParameter(parameter);

        // Then
        assertThat(ability).isEqualTo(Optional.of(TRAMPLE));
    }
}
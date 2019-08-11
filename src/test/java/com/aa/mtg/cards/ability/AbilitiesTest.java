package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.modifiers.PowerToughness;
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

    @Test
    public void testPowerAndToughnessFromParameter() {
        // Given
        String parameter = "+2/+2";

        // When
        PowerToughness powerToughness = powerToughnessFromParameter(parameter);

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(2, 2));
    }

    @Test
    public void testPowerAndToughnessFromParameterAbsent() {
        // Given
        String parameter = "TRAMPLE";

        // When
        PowerToughness powerToughness = powerToughnessFromParameter(parameter);

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(0, 0));
    }

    @Test
    public void testPowerAndToughnessFromParameters() {
        // Given
        List<String> parameters = asList("TRAMPLE", "+2/+2", "HASTE");

        // When
        PowerToughness powerToughness = powerToughnessFromParameters(parameters);

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(2, 2));
    }

    @Test
    public void testPowerAndToughnessFromParametersAbsent() {
        // Given
        String parameter = "TRAMPLE";

        // When
        PowerToughness powerToughness = powerToughnessFromParameter(parameter);

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(0, 0));
    }

    @Test
    public void testDamageFromParameter() {
        // Given
        String parameter = "DAMAGE:4";

        // When
        int damage = damageFromParameter(parameter);

        // Then
        assertThat(damage).isEqualTo(4);
    }

    @Test
    public void testDamageFromParameterAbsent() {
        // When
        int damage = damageFromParameter("TRAMPLE");

        // Then
        assertThat(damage).isEqualTo(0);
    }

    @Test
    public void testControllerDamageFromParameter() {
        // Given
        String parameter = "CONTROLLER_DAMAGE:5";

        // When
        int damage = controllerDamageFromParameter(parameter);

        // Then
        assertThat(damage).isEqualTo(5);
    }

    @Test
    public void testControllerDamageFromParameterAbsent() {
        // When
        int damage = controllerDamageFromParameter("TRAMPLE");

        // Then
        assertThat(damage).isEqualTo(0);
    }

    @Test
    public void testDestroyedFromParameter() {
        // Given
        String parameter = ":DESTROYED";

        // When
        boolean destroyed = destroyedFromParameter(parameter);

        // Then
        assertThat(destroyed).isTrue();
    }

    @Test
    public void testDestroyedFromParameterAbsent() {
        // When
        boolean destroyed = destroyedFromParameter("TRAMPLE");

        // Then
        assertThat(destroyed).isFalse();
    }

    @Test
    public void testTappedFromParameter() {
        // Given
        String parameter = ":TAPPED";

        // When
        boolean tapped = tapped(parameter);

        // Then
        assertThat(tapped).isTrue();
    }

    @Test
    public void testTappedFromParameterAbsent() {
        // When
        boolean tapped = tapped("TRAMPLE");

        // Then
        assertThat(tapped).isFalse();
    }

    @Test
    public void testTappedDoesNotUntapNextTurnFromParameter() {
        // Given
        String parameter = ":TAPPED_DOES_NOT_UNTAP_NEXT_TURN";

        // When
        boolean tappedDoesNotUntapNextTurn = tappedDoesNotUntapNextTurn(parameter);

        // Then
        assertThat(tappedDoesNotUntapNextTurn).isTrue();
    }

    @Test
    public void testTappedDoesNotUntapNextTurnFromParameterAbsent() {
        // When
        boolean tappedDoesNotUntapNextTurn = tappedDoesNotUntapNextTurn("TRAMPLE");

        // Then
        assertThat(tappedDoesNotUntapNextTurn).isFalse();
    }

    @Test
    public void testUntappedFromParameter() {
        // Given
        String parameter = ":UNTAPPED";

        // When
        boolean untapped = untapped(parameter);

        // Then
        assertThat(untapped).isTrue();
    }

    @Test
    public void testUntappedFromParameterAbsent() {
        // When
        boolean untapped = untapped("TRAMPLE");

        // Then
        assertThat(untapped).isFalse();
    }

    @Test
    public void testParametersAsString() {
        // Given
        List<String> parameters = asList("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE", "CONTROLLER_DAMAGE:3", ":TAPPED_DOES_NOT_UNTAP_NEXT_TURN", ":DESTROYED", ":RETURN_TO_OWNER_HAND");

        // When
        String parametersAsString = parametersAsString(parameters);

        // Then
        assertThat(parametersAsString).isEqualTo("+2/+2, trample, 2 damage, haste, to its controller 3 damage, tapped doesn't untap next turn, destroyed and returned to its owner's hand");
    }
}
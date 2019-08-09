package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.modifiers.PowerToughness;
import org.junit.Test;

import java.util.List;

import static com.aa.mtg.cards.ability.Abilities.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
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
    public void testPowerAndToughnessFromParameters() {
        // Given
        String parameter = "+2/+2";

        // When
        PowerToughness powerToughness = powerToughnessFromParameters(singletonList(parameter));

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(2, 2));
    }

    @Test
    public void testPowerAndToughnessFromParametersAbsent() {
        // When
        PowerToughness powerToughness = powerToughnessFromParameters(emptyList());

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(0, 0));
    }

    @Test
    public void testDamageFromParameters() {
        // Given
        String parameter = "DAMAGE:4";

        // When
        int damage = damageFromParameters(singletonList(parameter));

        // Then
        assertThat(damage).isEqualTo(4);
    }

    @Test
    public void testDamageFromParametersAbsent() {
        // When
        int damage = damageFromParameters(emptyList());

        // Then
        assertThat(damage).isEqualTo(0);
    }

    @Test
    public void testControllerDamageFromParameters() {
        // Given
        String parameter = "CONTROLLER_DAMAGE:5";

        // When
        int damage = controllerDamageFromParameters(singletonList(parameter));

        // Then
        assertThat(damage).isEqualTo(5);
    }

    @Test
    public void testControllerDamageFromParametersAbsent() {
        // When
        int damage = controllerDamageFromParameters(emptyList());

        // Then
        assertThat(damage).isEqualTo(0);
    }

    @Test
    public void testDestroyedFromParameters() {
        // Given
        String parameter = ":DESTROYED";

        // When
        boolean destroyed = destroyedFromParameters(singletonList(parameter));

        // Then
        assertThat(destroyed).isTrue();
    }

    @Test
    public void testDestroyedFromParametersAbsent() {
        // When
        boolean destroyed = destroyedFromParameters(emptyList());

        // Then
        assertThat(destroyed).isFalse();
    }

    @Test
    public void testTappedFromParameters() {
        // Given
        String parameter = ":TAPPED";

        // When
        boolean tapped = tapped(singletonList(parameter));

        // Then
        assertThat(tapped).isTrue();
    }

    @Test
    public void testTappedFromParametersAbsent() {
        // When
        boolean tapped = tapped(emptyList());

        // Then
        assertThat(tapped).isFalse();
    }

    @Test
    public void testTappedDoesNotUntapNextTurnFromParameters() {
        // Given
        String parameter = ":TAPPED_DOES_NOT_UNTAP_NEXT_TURN";

        // When
        boolean tappedDoesNotUntapNextTurn = tappedDoesNotUntapNextTurn(singletonList(parameter));

        // Then
        assertThat(tappedDoesNotUntapNextTurn).isTrue();
    }

    @Test
    public void testTappedDoesNotUntapNextTurnFromParametersAbsent() {
        // When
        boolean tappedDoesNotUntapNextTurn = tappedDoesNotUntapNextTurn(emptyList());

        // Then
        assertThat(tappedDoesNotUntapNextTurn).isFalse();
    }

    @Test
    public void testUntappedFromParameters() {
        // Given
        String parameter = ":UNTAPPED";

        // When
        boolean untapped = untapped(singletonList(parameter));

        // Then
        assertThat(untapped).isTrue();
    }

    @Test
    public void testUntappedFromParametersAbsent() {
        // When
        boolean untapped = untapped(emptyList());

        // Then
        assertThat(untapped).isFalse();
    }

    @Test
    public void testParametersAsString() {
        // Given
        List<String> parameters = asList("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE", "CONTROLLER_DAMAGE:3", ":TAPPED_DOES_NOT_UNTAP_NEXT_TURN", ":DESTROYED");

        // When
        String parametersAsString = parametersAsString(parameters);

        // Then
        assertThat(parametersAsString).isEqualTo("+2/+2, trample, 2 damage, haste, to its controller 3 damage, tapped doesn't untap next turn, destroyed");
    }
}
package com.matag.cards.ability;

import com.matag.cards.properties.PowerToughness;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class AbilityServiceTest {
    private AbilityService abilityService = new AbilityService();

    @Test
    public void testPowerAndToughnessFromParameter() {
        // Given
        String parameter = "+2/+2";

        // When
        PowerToughness PowerToughness = abilityService.powerToughnessFromParameter(parameter);

        // Then
        assertThat(PowerToughness).isEqualTo(new PowerToughness(2, 2));
    }

    @Test
    public void testPowerAndToughnessFromParameterAbsent() {
        // Given
        String parameter = "TRAMPLE";

        // When
        PowerToughness PowerToughness = abilityService.powerToughnessFromParameter(parameter);

        // Then
        assertThat(PowerToughness).isEqualTo(new PowerToughness(0, 0));
    }

    @Test
    public void testPowerAndToughnessFromParameters() {
        // Given
        List<String> parameters = asList("TRAMPLE", "+2/+2", "HASTE");

        // When
        PowerToughness PowerToughness = abilityService.powerToughnessFromParameters(parameters);

        // Then
        assertThat(PowerToughness).isEqualTo(new PowerToughness(2, 2));
    }

    @Test
    public void testPowerAndToughnessFromParametersAbsent() {
        // Given
        String parameter = "TRAMPLE";

        // When
        PowerToughness PowerToughness = abilityService.powerToughnessFromParameter(parameter);

        // Then
        assertThat(PowerToughness).isEqualTo(new PowerToughness(0, 0));
    }

    @Test
    public void testDamageFromParameter() {
        // Given
        String parameter = "DAMAGE:4";

        // When
        int damage = abilityService.damageFromParameter(parameter);

        // Then
        assertThat(damage).isEqualTo(4);
    }

    @Test
    public void testDamageFromParameterAbsent() {
        // When
        int damage = abilityService.damageFromParameter("TRAMPLE");

        // Then
        assertThat(damage).isEqualTo(0);
    }

    @Test
    public void testControllerDamageFromParameter() {
        // Given
        String parameter = "CONTROLLER_DAMAGE:5";

        // When
        int damage = abilityService.controllerDamageFromParameter(parameter);

        // Then
        assertThat(damage).isEqualTo(5);
    }

    @Test
    public void testControllerDamageFromParameterAbsent() {
        // When
        int damage = abilityService.controllerDamageFromParameter("TRAMPLE");

        // Then
        assertThat(damage).isEqualTo(0);
    }

    @Test
    public void testDestroyedFromParameter() {
        // Given
        String parameter = ":DESTROYED";

        // When
        boolean destroyed = abilityService.destroyedFromParameter(parameter);

        // Then
        assertThat(destroyed).isTrue();
    }

    @Test
    public void testDestroyedFromParameterAbsent() {
        // When
        boolean destroyed = abilityService.destroyedFromParameter("TRAMPLE");

        // Then
        assertThat(destroyed).isFalse();
    }

    @Test
    public void testTappedFromParameter() {
        // Given
        String parameter = ":TAPPED";

        // When
        boolean tapped = abilityService.tappedFromParameter(parameter);

        // Then
        assertThat(tapped).isTrue();
    }

    @Test
    public void testTappedFromParameterAbsent() {
        // When
        boolean tapped = abilityService.tappedFromParameter("TRAMPLE");

        // Then
        assertThat(tapped).isFalse();
    }

    @Test
    public void testTappedDoesNotUntapNextTurnFromParameter() {
        // Given
        String parameter = ":TAPPED_DOES_NOT_UNTAP_NEXT_TURN";

        // When
        boolean tappedDoesNotUntapNextTurn = abilityService.tappedDoesNotUntapNextTurnFromParameter(parameter);

        // Then
        assertThat(tappedDoesNotUntapNextTurn).isTrue();
    }

    @Test
    public void testTappedDoesNotUntapNextTurnFromParameterAbsent() {
        // When
        boolean tappedDoesNotUntapNextTurn = abilityService.tappedDoesNotUntapNextTurnFromParameter("TRAMPLE");

        // Then
        assertThat(tappedDoesNotUntapNextTurn).isFalse();
    }

    @Test
    public void testUntappedFromParameter() {
        // Given
        String parameter = ":UNTAPPED";

        // When
        boolean untapped = abilityService.untappedFromParameter(parameter);

        // Then
        assertThat(untapped).isTrue();
    }

    @Test
    public void testUntappedFromParameterAbsent() {
        // When
        boolean untapped = abilityService.untappedFromParameter("TRAMPLE");

        // Then
        assertThat(untapped).isFalse();
    }

    @Test
    public void testDrawFromParameter() {
        // Given
        String parameter = "DRAW:2";

        // When
        int draw = abilityService.drawFromParameter(parameter);

        // Then
        assertThat(draw).isEqualTo(2);
    }

    @Test
    public void testDrawFromParameterAbsent() {
        // When
        int draw = abilityService.drawFromParameter("TRAMPLE");

        // Then
        assertThat(draw).isEqualTo(0);
    }

    @Test
    public void testPlus1CountersFromParameter() {
        // Given
        String parameter = "PLUS_1_COUNTERS:2";

        // When
        int draw = abilityService.plus1CountersFromParameter(parameter);

        // Then
        assertThat(draw).isEqualTo(2);
    }

    @Test
    public void testPlus1CountersFromParameterAbsent() {
        // When
        int draw = abilityService.plus1CountersFromParameter("TRAMPLE");

        // Then
        assertThat(draw).isEqualTo(0);
    }

    @Test
    public void testParametersAsString() {
        // Given
        List<String> parameters = asList("+2/+2", "TRAMPLE", "DAMAGE:2", "HASTE", "CONTROLLER_DAMAGE:3", ":TAPPED_DOES_NOT_UNTAP_NEXT_TURN", ":DESTROYED", ":RETURN_TO_OWNER_HAND", "PLUS_1_COUNTERS:2");

        // When
        String parametersAsString = AbilityService.parametersAsString(parameters);

        // Then
        assertThat(parametersAsString).isEqualTo("+2/+2, trample, 2 damage, haste, to its controller 3 damage, tapped doesn't untap next turn, destroyed, returned to its owner's hand and 2 +1/+1 counters");
    }
}
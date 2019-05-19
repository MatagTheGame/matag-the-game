package com.aa.mtg.cards.ability.type;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;

public class AbilityTypeTest {
    @Test
    public void displayText() {
        // Given
        AbilityType abilityType = AbilityType.CREATURES_YOU_CONTROL_GET_PLUS_X_UNTIL_END_OF_TURN;
        List<String> parameters = singletonList("+1/+1");

        // When
        String text = abilityType.getText(parameters);

        // Then
        Assertions.assertThat(text).isEqualTo("Creatures you control get plus +1/+1 until end of turn.");
    }
}
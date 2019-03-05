package com.aa.mtg.cards;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.sets.RavnicaAllegiance;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.aa.mtg.cards.properties.Color.GREEN;
import static com.aa.mtg.cards.properties.Color.RED;
import static com.aa.mtg.cards.properties.Color.WHITE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CostUtilsTest {

    @Test
    public void isCastingCostFulfilledFeralMaakaCorrectColors() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Color> manaPaid = asList(GREEN, RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid);

        // Then
        assertThat(fulfilled).isTrue();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaNoMana() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Color> manaPaid = Collections.emptyList();

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaWrongColor() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Color> manaPaid = asList(WHITE, GREEN);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaOneLessMana() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Color> manaPaid = Collections.singletonList(RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledAxebaneBeastCorrectColors() {
        // Given
        Card card = RavnicaAllegiance.AXEBANE_BEAST;
        List<Color> manaPaid = asList(GREEN, GREEN, RED, RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid);

        // Then
        assertThat(fulfilled).isTrue();
    }

}
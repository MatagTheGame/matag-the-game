package com.aa.mtg.cards.cost;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.cards.CardsConfiguration;
import com.aa.mtg.cards.properties.Cost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static com.aa.mtg.cards.properties.Cost.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class CostUtilsTest {

    @Autowired
    private Cards cards;

    @Test
    public void isCastingCostFulfilledFeralMaakaCorrectCosts() {
        // Given
        Card card = cards.getCard("Feral Maaka");
        List<Cost> manaPaid = asList(GREEN, RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isTrue();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaNoMana() {
        // Given
        Card card = cards.getCard("Feral Maaka");
        List<Cost> manaPaid = Collections.emptyList();

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaWrongCost() {
        // Given
        Card card = cards.getCard("Feral Maaka");
        List<Cost> manaPaid = asList(WHITE, GREEN);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaOneLessMana() {
        // Given
        Card card = cards.getCard("Feral Maaka");
        List<Cost> manaPaid = Collections.singletonList(RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledAxebaneBeastCorrectCosts() {
        // Given
        Card card = cards.getCard("Axebane Beast");
        List<Cost> manaPaid = asList(GREEN, GREEN, RED, RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isTrue();
    }

}
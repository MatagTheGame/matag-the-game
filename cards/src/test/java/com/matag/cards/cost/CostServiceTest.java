package com.matag.cards.cost;

import com.matag.cards.Card;
import com.matag.cards.Cards;
import com.matag.cards.CardsConfiguration;
import com.matag.cards.properties.Cost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static com.matag.cards.properties.Cost.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class CostServiceTest {
    @Autowired
    private Cards cards;

    @Autowired
    private CostService costService;

    @Test
    public void isCastingCostFulfilledFeralMaakaCorrectCosts() {
        // Given
        Card card = cards.get("Feral Maaka");
        List<Cost> manaPaid = asList(GREEN, RED);

        // When
        boolean fulfilled = costService.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isTrue();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaNoMana() {
        // Given
        Card card = cards.get("Feral Maaka");
        List<Cost> manaPaid = Collections.emptyList();

        // When
        boolean fulfilled = costService.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaWrongCost() {
        // Given
        Card card = cards.get("Feral Maaka");
        List<Cost> manaPaid = asList(WHITE, GREEN);

        // When
        boolean fulfilled = costService.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaOneLessMana() {
        // Given
        Card card = cards.get("Feral Maaka");
        List<Cost> manaPaid = Collections.singletonList(RED);

        // When
        boolean fulfilled = costService.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledAxebaneBeastCorrectCosts() {
        // Given
        Card card = cards.get("Axebane Beast");
        List<Cost> manaPaid = asList(GREEN, GREEN, RED, RED);

        // When
        boolean fulfilled = costService.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isTrue();
    }

}
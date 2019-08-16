package integration.cards;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CostUtils;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.sets.RavnicaAllegiance;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static com.aa.mtg.cards.properties.Cost.GREEN;
import static com.aa.mtg.cards.properties.Cost.RED;
import static com.aa.mtg.cards.properties.Cost.WHITE;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CostUtilsTest {

    @Test
    public void isCastingCostFulfilledFeralMaakaCorrectCosts() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Cost> manaPaid = asList(GREEN, RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isTrue();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaNoMana() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Cost> manaPaid = Collections.emptyList();

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaWrongCost() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Cost> manaPaid = asList(WHITE, GREEN);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledFeralMaakaOneLessMana() {
        // Given
        Card card = RavnicaAllegiance.FERAL_MAAKA;
        List<Cost> manaPaid = Collections.singletonList(RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isFalse();
    }

    @Test
    public void isCastingCostFulfilledAxebaneBeastCorrectCosts() {
        // Given
        Card card = RavnicaAllegiance.AXEBANE_BEAST;
        List<Cost> manaPaid = asList(GREEN, GREEN, RED, RED);

        // When
        boolean fulfilled = CostUtils.isCastingCostFulfilled(card, manaPaid, null);

        // Then
        assertThat(fulfilled).isTrue();
    }

}
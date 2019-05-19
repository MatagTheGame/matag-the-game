package integration.cards.utils;

import com.aa.mtg.cards.modifiers.PowerToughness;
import org.junit.Test;

import static com.aa.mtg.cards.modifiers.PowerToughness.powerToughness;
import static org.assertj.core.api.Assertions.assertThat;

public class PowerToughnessTest {
    @Test
    public void powerToughnessPositiveTest() {
        // Given
        String powerToughnessString = "+1/+1";

        // When
        PowerToughness powerToughness = powerToughness(powerToughnessString);

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(1, 1));
    }

    @Test
    public void powerToughnessNegativeTest() {
        // Given
        String powerToughnessString = "-1/-1";

        // When
        PowerToughness powerToughness = powerToughness(powerToughnessString);

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(-1, -1));
    }

    @Test
    public void powerToughnessMixedTest() {
        // Given
        String powerToughnessString = "+2/-3";

        // When
        PowerToughness powerToughness = powerToughness(powerToughnessString);

        // Then
        assertThat(powerToughness).isEqualTo(new PowerToughness(2, -3));
    }

    @Test
    public void combine() {
        // Given
        PowerToughness first = powerToughness("+1/-2");
        PowerToughness second = powerToughness("-3/+4");

        // When
        PowerToughness combined = first.combine(second);

        // Then
        assertThat(combined).isEqualTo(new PowerToughness(-2, 2));
    }
}
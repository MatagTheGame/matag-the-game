package integration.mtg.cardinstance.modifiers;

import com.aa.mtg.cardinstance.modifiers.PowerToughness;
import org.junit.Test;

import static com.aa.mtg.cardinstance.modifiers.PowerToughness.powerToughness;
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
}
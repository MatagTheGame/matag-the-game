package integration.cardinstance.modifiers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.matag.game.cardinstance.modifiers.Counters;

public class CountersTest {
  private Counters counters = new Counters();

  @Test
  public void addPlus1Counters() {
    // When
    counters.addPlus1Counters(1);

    // Then
    assertThat(counters.getPlus1Counters()).isEqualTo(1);
  }

  @Test
  public void addMinus1Counters() {
    // When
    counters.addMinus1Counters(2);

    // Then
    assertThat(counters.getMinus1Counters()).isEqualTo(2);
  }

  @Test
  public void countersCancelsEachOther() {
    // When
    counters.addPlus1Counters(2);
    counters.addMinus1Counters(1);

    // Then
    assertThat(counters.getPlus1Counters()).isEqualTo(1);
    assertThat(counters.getMinus1Counters()).isEqualTo(0);
  }
}
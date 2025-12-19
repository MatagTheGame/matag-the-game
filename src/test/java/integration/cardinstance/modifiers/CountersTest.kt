package integration.cardinstance.modifiers

import com.matag.game.cardinstance.modifiers.Counters
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CountersTest {
    private val counters = Counters()

    @Test
    fun addPlus1Counters() {
        // When
        counters.addPlus1Counters(1)

        // Then
        assertThat(counters.plus1Counters).isEqualTo(1)
    }

    @Test
    fun addMinus1Counters() {
        // When
        counters.addMinus1Counters(2)

        // Then
        assertThat(counters.minus1Counters).isEqualTo(2)
    }

    @Test
    fun countersCancelsEachOther() {
        // When
        counters.addPlus1Counters(2)
        counters.addMinus1Counters(1)

        // Then
        assertThat(counters.plus1Counters).isEqualTo(1)
        assertThat(counters.minus1Counters).isEqualTo(0)
    }
}
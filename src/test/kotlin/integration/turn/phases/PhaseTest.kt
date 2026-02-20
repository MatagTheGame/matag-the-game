package integration.turn.phases

import com.matag.game.MatagGameApplication
import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.PhaseFactory
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class PhaseTest(
    val phaseFactory: PhaseFactory,
    val testUtils: TestUtils
) {
    @ParameterizedTest
    @CsvSource(
        "UT,UP",
        "EC,M2",
        "CL,UT",
        )
    fun nextPhaseUntap(phase: String, expectedNextPhase: String) {
        // Given
        val untapPhase = phaseFactory.get(phase)

        // When
        val nextPhase: Phase = untapPhase.getNextPhase(testUtils.testGameStatus())

        // Then
        assertThat(nextPhase.name ?: "null").isEqualTo(expectedNextPhase)
    }
}
package integration.turn.phases

import com.matag.game.turn.phases.Phase
import com.matag.game.turn.phases.PhaseFactory
import integration.TestUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [PhasesTestConfiguration::class])
class PhaseTest(
    @param:Autowired val phaseFactory: PhaseFactory,
    @param:Autowired val testUtils: TestUtils
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
        val nextPhase: Phase? = untapPhase.getNextPhase(testUtils.testGameStatus())

        // Then
        assertThat(nextPhase?.getName ?: "null").isEqualTo(expectedNextPhase)
    }
}
package integration.turn.action.player

import com.matag.game.MatagGameApplication
import com.matag.game.turn.action.finish.FinishGameService
import com.matag.game.turn.action.player.LifeService
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestUtilsConfiguration::class)
class LifeServiceTest(
    private val lifeService: LifeService,
    private val testUtils: TestUtils,
    private val finishGameService: FinishGameService
) {
    

    @Test
    fun addLife() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val amount = 1

        // When
        lifeService.add(gameStatus.player1!!, amount, gameStatus)

        // Then
        assertThat(gameStatus.player1?.life).isEqualTo(21)
    }

    @Test
    fun loseLifeAndLoseGame() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val amount = -25

        // When
        lifeService.add(gameStatus.player1!!, amount, gameStatus)

        // Then
        assertThat(gameStatus.player1?.life).isEqualTo(-5)
        Mockito.verify(finishGameService).setWinner(gameStatus, gameStatus.player2!!)
    }
}
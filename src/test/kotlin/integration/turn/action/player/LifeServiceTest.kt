package integration.turn.action.player

import com.matag.game.turn.action.finish.FinishGameService
import com.matag.game.turn.action.player.LifeService
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [PlayerTestConfiguration::class, TestUtilsConfiguration::class])
class LifeServiceTest(
    @param:Autowired private val lifeService: LifeService,
    @param:Autowired private val testUtils: TestUtils,
    @param:Autowired private val finishGameService: FinishGameService
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
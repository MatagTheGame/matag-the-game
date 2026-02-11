package integration.turn.action.player

import com.matag.game.turn.action.finish.FinishGameService
import com.matag.game.turn.action.player.LifeService
import integration.TestUtils
import integration.TestUtilsConfiguration
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [PlayerTestConfiguration::class, TestUtilsConfiguration::class])
class LifeServiceTest {
    @Autowired
    private val lifeService: LifeService? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val finishGameService: FinishGameService? = null

    @Test
    fun addLife() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val amount = 1

        // When
        lifeService!!.add(gameStatus.getPlayer1(), amount, gameStatus)

        // Then
        Assertions.assertThat(gameStatus.getPlayer1().life).isEqualTo(21)
    }

    @Test
    fun loseLifeAndLoseGame() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val amount = -25

        // When
        lifeService!!.add(gameStatus.getPlayer1(), amount, gameStatus)

        // Then
        Assertions.assertThat(gameStatus.getPlayer1().life).isEqualTo(-5)
        Mockito.verify<FinishGameService?>(finishGameService).setWinner(gameStatus, gameStatus.getPlayer2())
    }
}
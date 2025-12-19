package integration.turn.action.player

import com.matag.game.cardinstance.CardInstance
import com.matag.game.turn.action.finish.FinishGameService
import com.matag.game.turn.action.player.DrawXCardsService
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
class DrawXCardsServiceTest {
    @Autowired
    private val drawXCardsService: DrawXCardsService? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val finishGameService: FinishGameService? = null

    @Test
    fun drawCards() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val amount = 2

        // When
        drawXCardsService!!.drawXCards(gameStatus.getPlayer1(), amount, gameStatus)

        // Then
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().getHand().getCards()).hasSize(9)
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().getLibrary().getCards()).hasSize(31)
    }

    @Test
    fun drawFromEmptyLibrary() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val firstTwoCards = gameStatus.getPlayer1().getLibrary().getCards().subList(0, 2)
        gameStatus.getPlayer1().getLibrary().setCards(firstTwoCards)
        val amount = 3

        // When
        drawXCardsService!!.drawXCards(gameStatus.getPlayer1(), amount, gameStatus)

        // Then
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().getHand().getCards()).hasSize(9)
        Assertions.assertThat<CardInstance?>(gameStatus.getPlayer1().getLibrary().getCards()).hasSize(0)
        Mockito.verify<FinishGameService?>(finishGameService).setWinner(gameStatus, gameStatus.getPlayer2())
    }
}
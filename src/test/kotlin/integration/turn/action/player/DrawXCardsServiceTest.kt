package integration.turn.action.player

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
class DrawXCardsServiceTest(
    @param:Autowired private val drawXCardsService: DrawXCardsService,
    @param:Autowired private val testUtils: TestUtils,
    @param:Autowired private val finishGameService: FinishGameService
) {


    @Test
    fun drawCards() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val amount = 2

        // When
        drawXCardsService.drawXCards(gameStatus.player1!!, amount, gameStatus)

        // Then
        Assertions.assertThat(gameStatus.player1?.hand?.cards).hasSize(9)
        Assertions.assertThat(gameStatus.player1?.library?.cards).hasSize(31)
    }

    @Test
    fun drawFromEmptyLibrary() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val firstTwoCards = gameStatus.player1!!.library.cards.subList(0, 2)
        gameStatus.player1!!.library.cards = firstTwoCards
        val amount = 3

        // When
        drawXCardsService.drawXCards(gameStatus.player1!!, amount, gameStatus)

        // Then
        Assertions.assertThat(gameStatus.player1?.hand?.cards).hasSize(9)
        Assertions.assertThat(gameStatus.player1?.library?.cards).hasSize(0)
        Mockito.verify(finishGameService).setWinner(gameStatus, gameStatus.player2!!)
    }
}
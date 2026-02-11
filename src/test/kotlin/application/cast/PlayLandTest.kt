package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PlayLandTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun playLand() {
        // When play first land
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards!!.get("Island")).click()

        // Then battlefields contain land
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .containsExactly(cards.get("Island"))
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.FIRST_LINE)
            .containsExactly(cards.get("Island"))

        // Hand is empty
        browser.player1().getHandHelper(PlayerType.PLAYER).toHaveSize(1)
        browser.player2().getHandHelper(PlayerType.OPPONENT).toHaveSize(1)

        // When play second land
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Island")).click()

        // Then error is displayed
        browser.player1().messageHelper.hasMessage("You already played a land this turn.")
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"))
        }
    }
}

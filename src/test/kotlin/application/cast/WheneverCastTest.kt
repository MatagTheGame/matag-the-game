package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class WheneverCastTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun wheneverACreatureEntersTheBattlefieldAbility() {
        // When playing Precision Bolt
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Mountain"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Precision Bolt")).select()
        browser.player1().getPlayerInfoHelper(PlayerType.OPPONENT).click()

        // Then Adeliz gets activated
        browser.player1().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        val adelizId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getFirstCard(cards.get("Adeliz, the Cinder Wind")).cardIdNumeric
        browser.player1().stackHelper.containsAbility("Player1's Adeliz, the Cinder Wind (" + adelizId + "): Wizards you control get +1/+1 until end of turn.")

        // When Adeliz triggered ability resolves
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)

        // Then Adeliz power is increased
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Adeliz, the Cinder Wind")).hasPowerAndToughness("3/3")

        // When Precision Bolt
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Opponent loses 3 life
        browser.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(17)
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Adeliz, the Cinder Wind"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Precision Bolt"))
        }
    }
}

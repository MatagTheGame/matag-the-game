package application.counters

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

class PutPlusOneCountersOnCreaturesTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun putPlusOneCountersOnCreaturesTest() {
        // When cast Gird for Battle targeting two creatures player control
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Gird for Battle")).select()
        browser.player1().statusHelper.hasMessage("Select targets for Gird for Battle.")
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).target()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 1).target()

        // Sorcery goes on the stack
        browser.player1().stackHelper.containsExactly(cards.get("Gird for Battle"))

        // When opponent accepts
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Then the counters are added on both creatures
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).hasPlus1Counters(1)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).hasPowerAndToughness("2/4")
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 1).hasPlus1Counters(1)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 1).hasPowerAndToughness("2/4")

        // When cast Gird for Battle targeting twice the same creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Gird for Battle")).select()
        browser.player1().statusHelper.hasMessage("Select targets for Gird for Battle.")
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).target()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).click()

        // An error is displayed
        browser.messageHelper.hasMessage("Targets must be different.")
        browser.messageHelper.close()

        // When cast the sorcery targeting only one creature
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Gird for Battle")).select()
        browser.player1().statusHelper.hasMessage("Select targets for Gird for Battle.")
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).target()
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)

        // Sorcery goes on the stack
        browser.player1().stackHelper.containsExactly(cards.get("Gird for Battle"))

        // When opponent accepts
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Then the counter is added on the creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).hasPlus1Counters(2)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Concordia Pegasus"), 0).hasPowerAndToughness("3/5")
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Gird for Battle"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Gird for Battle"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"))
        }
    }
}

package application.countertarget

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

class CounterTargetSpellTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun counterTargetSpellTest() {
        // Player1 casts a creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Concordia Pegasus")).click()

        // Player2 counters it
        browser.player2().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Island"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Island"), 1).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Island"), 2).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Cancel")).select()
        browser.player2().stackHelper.getFirstCard(cards.get("Concordia Pegasus")).click()

        // Player1 accepts it
        browser.player1().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)

        // Both spells are off the stack
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Cancel"))
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Concordia Pegasus"))
        browser.player1().stackHelper.isEmpty
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE).isEmpty
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Concordia Pegasus"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))

            addCardToNonCurrentPlayerHand(gameStatus, cards.get("Cancel"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
        }
    }
}

package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CastMultipleInstantsTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun castInstantPoweringCreatureDuringCombat() {
        // When player 1 try to deal 5 damage to a creature player 2 owns
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Mountain"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 2).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 3).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Engulfing Eruption")).click()
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Douser of Lights")).target()
        browser.player1().stackHelper.contains(cards.get("Engulfing Eruption"))

        // Then player 2 can cast a spell to reinforce its creature and don't let it die
        browser.player2().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Dark Remedy")).click()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Douser of Lights")).target()
        browser.player2().stackHelper.contains(cards.get("Engulfing Eruption"), cards.get("Dark Remedy"))

        // Player 1 continues
        browser.player1().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Douser of Lights")).hasPowerAndToughness("5/8")
        browser.player1().stackHelper.contains(cards.get("Engulfing Eruption"))
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Dark Remedy"))

        // Players 2 continues
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Douser of Lights")).hasPowerAndToughness("5/8")
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player2().stackHelper.isEmpty
        browser.player2().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Engulfing Eruption"))
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Engulfing Eruption"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))

            addCardToNonCurrentPlayerHand(gameStatus, cards.get("Dark Remedy"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Douser of Lights"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
        }
    }
}

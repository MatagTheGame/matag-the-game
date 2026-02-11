package application.selection

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

class AllOtherCreaturesYouControlAbilityTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun allOtherCreaturesAbilityTest() {
        // Creatures have basic power
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards!!.get("Enforcer Griffin")).hasPowerAndToughness("3/4")
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("3/4")

        // When Benalish Marshal is on the battlefield
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 0).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser!!.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Benalish Marshal")).click()
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Players Creatures only have increased power
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Benalish Marshal")).hasPowerAndToughness("3/3")
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("4/5")
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("3/4")
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Benalish Marshal"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Enforcer Griffin"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Enforcer Griffin"))
        }
    }
}

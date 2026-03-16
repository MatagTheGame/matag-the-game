package application.selection

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Import(AllOtherCreaturesYouControlAbilityTest.InitTestServiceForTest::class)
class AllOtherCreaturesYouControlAbilityTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun allOtherCreaturesAbilityTest() {
        // Creatures have basic power
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards!!.get("Enforcer Griffin")).hasPowerAndToughness("3/4")
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("3/4")

        // When Benalish Marshal is on the battlefield
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 0).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser!!.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Benalish Marshal")).click()
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Players Creatures only have increased power
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Benalish Marshal")).hasPowerAndToughness("3/3")
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("4/5")
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("3/4")
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
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

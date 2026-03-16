package application.countertarget

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

@Import(CounterTargetSpellTest.InitTestServiceForTest::class)
class CounterTargetSpellTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun counterTargetSpellTest() {
        // Player1 casts a creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Concordia Pegasus")).click()

        // Player2 counters it
        browser.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 1).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 2).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Cancel")).select()
        browser.player2().getStackHelper().getFirstCard(cards.get("Concordia Pegasus")).click()

        // Player1 accepts it
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)

        // Both spells are off the stack
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Cancel"))
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Concordia Pegasus"))
        browser.player1().getStackHelper().isEmpty
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE).isEmpty
    }

    class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
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

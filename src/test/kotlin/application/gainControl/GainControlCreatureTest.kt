package application.gainControl

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
class GainControlCreatureTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun gainControlCreature() {
        // When cast the sorcery
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Mountain"), 0).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 2).tap()
        browser!!.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Act of Treason")).select()
        browser!!.player1().statusHelper.hasMessage("Select targets for Act of Treason.")
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Concordia Pegasus")).click()

        // Sorcery goes on the stack
        browser!!.player1().stackHelper.containsExactly(cards.get("Act of Treason"))

        // When opponent accepts
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Player1 now control the creature
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE).isEmpty
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .containsExactly(cards.get("Concordia Pegasus"))
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Concordia Pegasus")).isNotTapped
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Concordia Pegasus")).doesNotHaveSummoningSickness()
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Act of Treason"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Concordia Pegasus"))
            gameStatus.nonCurrentPlayer.battlefield.cards.get(0).modifiers.setSummoningSickness(true)
            gameStatus.nonCurrentPlayer.battlefield.cards.get(0).modifiers.setTapped(true)
        }
    }
}

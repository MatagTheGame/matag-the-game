package application.cleanup

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.ending.CleanupPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class DiscardACardAtEndOfTurnIfMoreThanSevenTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun discardACardAtEndOfTurnIfMoreThanSeven() {
        // When arriving to End of Turn with 9 cards
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)
        browser.player1().actionHelper.clickContinueAndExpectPhase(CleanupPhase.Companion.CL, PlayerType.PLAYER)
        browser.player1().getHandHelper(PlayerType.PLAYER).toHaveSize(9)

        // It stops asking to discard a card
        browser.player1().statusHelper.hasMessage("Choose a card to discard.")
        browser.player1().actionHelper.clickContinue()
        browser.player1().messageHelper.hasMessage("Cannot continue: DISCARD_A_CARD")
        browser.player1().messageHelper.close()

        // When player clicks on a card to discard
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards!!.get("Plains")).select()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Mountain")).click()

        // Then that card is discarded (down to 7)
        browser.getHandHelper(PlayerType.PLAYER).toHaveSize(7)
        browser.getGraveyardHelper(PlayerType.PLAYER).toHaveSize(2)
        browser.getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Plains"), cards.get("Mountain"))

        // Priority is finally passed
        browser.player1().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.OPPONENT)
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            // Current Player
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"))

            // Non Current Player
            addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"))
        }
    }
}

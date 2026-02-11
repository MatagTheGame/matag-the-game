package application.scry

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
class ScryAbilityTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun scryAbilityTest() {
        // Given Player Plays Get the Point
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getFirstCard(cards!!.get("Mountain")).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 2).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 3).tap()
        browser!!.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Get the Point")).select()
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Feral Maaka")).click()

        // And Opponent accepts it
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Player can see the top card
        browser!!.player1().getLibraryHelper(PlayerType.PLAYER).visibleCardsHelper.contains("Swamp")
        browser!!.player1().getLibraryHelper(PlayerType.PLAYER).visibleCardsHelper.getFirstCard(cards.get("Swamp"))
            .click()

        // FIXME now clicking on the card should show two options: keep on top or put to bottom.
        println()
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Feral Maaka"))
            addCardToNonCurrentPlayerLibrary(gameStatus, cards!!.get("Mountain"))
            addCardToNonCurrentPlayerLibrary(gameStatus, cards!!.get("Mountain"))

            addCardToCurrentPlayerLibrary(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerLibrary(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Get the Point"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
        }
    }
}

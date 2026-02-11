package application.enter

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CreatureEntersTheBattlefieldWithTargetAbilityTapDoesNotUntapTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun creatureEntersTheBattlefieldWithTargetAbilityTapDoesNotUntapTest() {
        // When Playing Frost Lynx
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Island"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Island"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Island"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Frost Lynx")).click()
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Player 1 cannot resolve without choosing a target if there are targets available
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)
        val frostLynxId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getFirstCard(cards.get("Frost Lynx")).cardIdNumeric
        browser.player1().messageHelper.hasMessage("\"" + frostLynxId + " - Frost Lynx\" requires a valid target.")
        browser.player1().messageHelper.close()

        // Player 1 chooses a target
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Canopy Spider")).click()
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Canopy Spider")).isTargeted

        // Player 2 just continues
        browser.player2().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Canopy Spider")).isTargeted
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Player 1 has priority again and target is tapped
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Canopy Spider")).isTappedDoesNotUntapNextTurn

        // Next turn target is still tapped
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Canopy Spider")).isTapped
        browser.player2().getHandHelper(PlayerType.PLAYER).contains(cards.get("Forest"))
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.OPPONENT)
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Next next turn target is still tapped
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Canopy Spider")).isTapped
        browser.player1().getHandHelper(PlayerType.PLAYER).contains(cards.get("Island"))
        browser.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)

        // Next next next turn target is untapped
        browser.player2().getHandHelper(PlayerType.PLAYER).contains(cards.get("Forest"), cards.get("Forest"))
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Canopy Spider")).isNotTapped
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Frost Lynx"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Canopy Spider"))
            addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Forest"))
            addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Forest"))
        }
    }
}

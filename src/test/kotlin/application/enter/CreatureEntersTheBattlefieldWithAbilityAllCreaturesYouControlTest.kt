package application.enter

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CreatureEntersTheBattlefieldWithAbilityAllCreaturesYouControlTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun creatureEntersTheBattlefieldWithAbilityAllCreaturesYouControl() {
        // When Playing Angel of the Dawn
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 3).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 4).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Angel of the Dawn")).click()
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)
        val angelOfTheDawnId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getFirstCard(cards.get("Angel of the Dawn")).cardIdNumeric

        // Then the enter the battlefield event gets triggered
        browser.player1().stackHelper.containsAbility("Player1's Angel of the Dawn (" + angelOfTheDawnId + "): Creatures you control get +1/+1 and vigilance until end of turn.")
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Which increases other creatures
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("4/5")

        // And gives them vigilance
        browser.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).declareAsAttacker()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).isNotTapped
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Angel of the Dawn"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Enforcer Griffin"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
        }
    }
}

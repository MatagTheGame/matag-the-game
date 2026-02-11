package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class WhenAttackTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun combatTrampleHaste() {
        // going to attack
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // attacking with a creature with when attacks ability
        val brazenWolvesId =
            browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getFirstCard(cards!!.get("Brazen Wolves")).cardIdNumeric
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Brazen Wolves")).declareAsAttacker()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // attacking ability is on the stack
        browser!!.player1().stackHelper.containsAbility("Player1's Brazen Wolves (" + brazenWolvesId + "): Gets +2/+0 until end of turn.")
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.OPPONENT
        )
        browser!!.player2().stackHelper.containsAbility("Player1's Brazen Wolves (" + brazenWolvesId + "): Gets +2/+0 until end of turn.")
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // continuing
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)
        browser!!.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(16)
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Brazen Wolves"))
        }
    }
}

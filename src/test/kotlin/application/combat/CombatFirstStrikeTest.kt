package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.*
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CombatFirstStrikeTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun combatFirstStrike() {
        // Stops to play instants
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.PLAYER)
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // When attacking
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards!!.get("Youthful Knight")).declareAsAttacker()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.OPPONENT
        )

        // Stop to play instants
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // And blocking
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Coral Merfolk")).declareAsBlocker()
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.PLAYER
        )

        // Then stop to play instants before first strike
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // Then stop to play instants after first strike, before combat damage and at end of combat
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(FirstStrikePhase.Companion.FS, PlayerType.PLAYER)
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(FirstStrikePhase.Companion.FS, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(CombatDamagePhase.Companion.CD, PlayerType.PLAYER)
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            CombatDamagePhase.Companion.CD,
            PlayerType.OPPONENT
        )
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(EndOfCombatPhase.Companion.EC, PlayerType.PLAYER)
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(EndOfCombatPhase.Companion.EC, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)

        // Then only the non first strike creature dies
        browser!!.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Coral Merfolk"))
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Youthful Knight"))
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Youthful Knight"))
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Bladebrand"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Coral Merfolk"))
            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Bladebrand"))
        }
    }
}

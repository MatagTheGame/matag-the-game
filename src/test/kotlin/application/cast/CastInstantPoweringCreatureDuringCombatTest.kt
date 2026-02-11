package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.BeginCombatPhase
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CastInstantPoweringCreatureDuringCombatTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun castInstantPoweringCreatureDuringCombat() {
        // When player one attack thinking to win the fight
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)
        browser.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards!!.get("Bastion Enforcer")).declareAsAttacker()
        browser.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.OPPONENT
        )

        // And player 2 plays the game by blocking
        browser.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Bartizan Bats")).declareAsBlocker()
        browser.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // Then player 2 can pump up its creature
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Dark Remedy")).select()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Bartizan Bats")).target()

        // And spell goes on the stack
        browser.player2().stackHelper.contains(cards.get("Dark Remedy"))
        browser.player1().stackHelper.contains(cards.get("Dark Remedy"))

        // When player 1 continue
        browser.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // Then spell is resolved from the stack and creature gets the +1/+3
        browser.player1().stackHelper.isEmpty
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Dark Remedy"))
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Bartizan Bats")).hasPowerAndToughness("4/4")
        browser.player1().phaseHelper.`is`(DeclareBlockersPhase.Companion.DB, PlayerType.OPPONENT)
        browser.player2().stackHelper.isEmpty
        browser.player2().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Dark Remedy"))
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Bartizan Bats")).hasPowerAndToughness("4/4")
        browser.player2().phaseHelper.`is`(DeclareBlockersPhase.Companion.DB, PlayerType.PLAYER)

        // When player 2 continues
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)

        // Then combat is ended
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Bartizan Bats")).hasDamage(3)
        browser.player2().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Bastion Enforcer"))

        // When moving to next turn
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)

        // Then extra power and toughness is cleaned up
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Bartizan Bats")).hasPowerAndToughness("3/1")
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Bastion Enforcer"))

            addCardToNonCurrentPlayerHand(gameStatus, cards.get("Dark Remedy"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Bartizan Bats"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Swamp"))
        }
    }
}

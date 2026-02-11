package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.*
import com.matag.game.turn.phases.ending.EndTurnPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CombatIndestructibleTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun indestructible() {
        // When going to combat
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.PLAYER)
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // When declare attacker
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards!!.get("Nyxborn Courser")).declareAsAttacker()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nyxborn Marauder")).declareAsAttacker()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.OPPONENT
        )

        // Declare blocker
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Nyxborn Courser")).select()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nyxborn Brute")).declareAsBlocker()
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Nyxborn Marauder")).select()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nyxborn Colossus")).declareAsBlocker()
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.PLAYER
        )

        // Make a Stand
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 0).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser!!.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Make a Stand")).click()
        browser!!.player1().phaseHelper.`is`(DeclareBlockersPhase.Companion.DB, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.PLAYER
        )
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            CombatDamagePhase.Companion.CD,
            PlayerType.OPPONENT
        )
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(EndOfCombatPhase.Companion.EC, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)

        // Then
        browser!!.player1().getGraveyardHelper(PlayerType.PLAYER).containsExactly(cards.get("Make a Stand"))
        browser!!.player1().getGraveyardHelper(PlayerType.OPPONENT).containsExactly(cards.get("Nyxborn Brute"))

        // Finally indestructible still gets destroyed if toughness reaches 0
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(EndTurnPhase.Companion.ET, PlayerType.OPPONENT)
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser!!.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Disfigure")).select()
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nyxborn Marauder")).target()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(EndTurnPhase.Companion.ET, PlayerType.OPPONENT)
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser!!.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Disfigure")).select()
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nyxborn Marauder")).target()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(EndTurnPhase.Companion.ET, PlayerType.OPPONENT)
        browser!!.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Nyxborn Marauder"))
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Nyxborn Courser"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Nyxborn Marauder"))
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Make a Stand"))


            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Disfigure"))
            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Disfigure"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Nyxborn Colossus"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Nyxborn Brute"))
            addCardToNonCurrentPlayerLibrary(gameStatus, cards!!.get("Swamp"))
        }
    }
}

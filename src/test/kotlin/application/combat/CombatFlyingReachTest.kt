package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CombatFlyingReachTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun combatFlyingReach() {
        // When going to combat
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // creature with flying should have the correct class
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards!!.get("Air Elemental")).hasFlying()

        // When declare attacker
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).declareAsAttacker()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).declareAsAttacker()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).declareAsAttacker()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // Declare blocker
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getCard(cards.get("Air Elemental"), 0).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Air Elemental")).click()

        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getCard(cards.get("Air Elemental"), 1).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).click()

        val airElemental =
            browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
                .getCard(cards.get("Air Elemental"), 2)
        airElemental.click()
        val ancientBrontodon =
            browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getFirstCard(cards.get("Ancient Brontodon"))
        ancientBrontodon.click()
        browser!!.player2().messageHelper.hasMessage("\"" + ancientBrontodon.cardIdNumeric + " - Ancient Brontodon\" cannot block \"" + airElemental.cardIdNumeric + " - Air Elemental\" as it has flying.")
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Air Elemental"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Grazing Whiptail"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Ancient Brontodon"))
        }
    }
}

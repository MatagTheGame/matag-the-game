package application.combat

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

class CombatUntappingBlockerTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun combatUntappingBlocker() {
        // Player1 attacks
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards!!.get("Headwater Sentries")).click()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.OPPONENT
        )

        // Player2 before going to blocking untap its creature
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Forest"), 0).tap()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Forest"), 1).tap()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Forest"), 2).tap()
        browser!!.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Spidery Grasp")).select()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).click()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.OPPONENT
        )
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // Now Player2 can block
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).click()
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)
        browser!!.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Headwater Sentries"))
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Headwater Sentries"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Forest"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Forest"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Forest"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Ancient Brontodon"))
            gameStatus.nonCurrentPlayer.battlefield.search().withName("Ancient Brontodon").cards.get(0)
                .modifiers.setTapped(true)
            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Spidery Grasp"))
        }
    }
}

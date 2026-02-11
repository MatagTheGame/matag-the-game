package application.leave

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.BeginCombatPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreatureDiesAbilityTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun creatureDiesAbility() {
        val firstGoblinId =
            browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getCard(cards!!.get("Goblin Assault Team"), 0).cardIdNumeric
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Goblin Assault Team"), 1).cardIdNumeric

        // When opponent kills 1 goblin
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 2).tap()
        browser!!.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Murder")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).click()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)

        // Then put +1/+1 counter is triggered
        browser!!.player1().stackHelper.containsAbilitiesExactly(mutableListOf<String?>("Player1's Goblin Assault Team (" + firstGoblinId + "): That targets get 1 +1/+1 counters."))

        // When clicking on the other goblin
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.PLAYER)
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).target()
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.PLAYER)

        // Then that goblin gets a counter
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).hasPlus1Counters(1)
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Goblin Assault Team")).hasPowerAndToughness("5/2")
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Goblin Assault Team"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Goblin Assault Team"))

            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Murder"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
        }
    }
}

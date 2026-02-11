package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CombatTrampleHasteTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun combatTrampleHaste() {
        // Playing card with trample haste
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Mountain"), 0).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 2).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 3).tap()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Mountain"), 4).tap()
        browser!!.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Charging Monstrosaur")).click()
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // When going to combat
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // When declare attacker
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Charging Monstrosaur")).declareAsAttacker()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // Declare blocker
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Charging Monstrosaur")).select()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Huatli's Snubhorn")).declareAsBlocker()
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)

        // Then
        browser!!.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(17)

        browser!!.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Huatli's Snubhorn"))
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Charging Monstrosaur")).hasDamage(2)
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Charging Monstrosaur"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Huatli's Snubhorn"))
        }
    }
}

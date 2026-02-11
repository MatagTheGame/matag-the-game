package application.ability

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
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ActivatedAbilityOnCreatureTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun activatedAbilityOnCreature() {
        // Playing jousting dummy
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Jousting Dummy")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // When increasing jousting dummy (as well on summoning sickness creature)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 3).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 4).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Jousting Dummy"), 1).click()

        val secondJoustingDummyId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getCard(cards.get("Jousting Dummy"), 1).getCardIdNumeric()

        // then ability goes on the stack
        browser.player1().getStackHelper()
            .containsAbility("Player1's Jousting Dummy (" + secondJoustingDummyId + "): Gets +1/+0 until end of turn.")

        // opponent accepts the ability
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // power of jousting dummy is increased
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Jousting Dummy"), 1).hasSummoningSickness()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Jousting Dummy"), 1).hasPowerAndToughness("3/1")

        // move at AfterBlocking phase
        browser.player1().getActionHelper()
            .clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.PLAYER)
        browser.player1().getActionHelper()
            .clickContinueAndExpectPhase(DeclareAttackersPhase.Companion.DA, PlayerType.PLAYER)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Jousting Dummy")).declareAsAttacker()
        browser.player1().getActionHelper()
            .clickContinueAndExpectPhase(DeclareAttackersPhase.Companion.DA, PlayerType.PLAYER)
        browser.player1().getActionHelper()
            .clickContinueAndExpectPhase(DeclareBlockersPhase.Companion.DB, PlayerType.PLAYER)

        // check can increase jousting dummy at instant speed
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 5).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 6).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 7).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.COMBAT_LINE)
            .getCard(cards.get("Jousting Dummy"), 0).click()

        val firstJoustingDummyId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.COMBAT_LINE)
                .getCard(cards.get("Jousting Dummy"), 0).getCardIdNumeric()

        // then ability goes on the stack
        browser.player1().getStackHelper()
            .containsAbility("Player1's Jousting Dummy (" + firstJoustingDummyId + "): Gets +1/+0 until end of turn.")

        // opponent accepts the ability
        browser.player2().getActionHelper()
            .clickContinueAndExpectPhase(DeclareBlockersPhase.Companion.DB, PlayerType.PLAYER)

        // power of jousting dummy is increased
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.COMBAT_LINE)
            .getCard(cards.get("Jousting Dummy"), 0).doesNotHaveSummoningSickness()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.COMBAT_LINE)
            .getCard(cards.get("Jousting Dummy"), 0).hasPowerAndToughness("3/1")
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Jousting Dummy"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Jousting Dummy"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
        }
    }
}

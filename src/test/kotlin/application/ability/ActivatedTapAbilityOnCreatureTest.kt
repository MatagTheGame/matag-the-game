package application.ability

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ActivatedTapAbilityOnCreatureTest : AbstractApplicationTest() {
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
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Checkpoint Officer")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Prepare the mana
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 3).tap()

        // Playing a tapping ability on a tapped creature does nothing
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Checkpoint Officer"), 0).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Checkpoint Officer"), 0).isNotSelected()

        // Playing a tapping ability on a creature with summoning sickness does nothing
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Checkpoint Officer"), 2).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Checkpoint Officer"), 2).isNotSelected()

        // Playing a tapping ability on a creature with summoning sickness does nothing
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Checkpoint Officer"), 1).select()
        val checkpointOfficerId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getCard(cards.get("Checkpoint Officer"), 1).getCardIdNumeric()
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Jousting Dummy")).target()

        // Ability goes on the stack
        browser.player2().getStackHelper()
            .containsAbility("Player1's Checkpoint Officer (" + checkpointOfficerId + "): That targets get tapped.")
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Both creatures are tapped
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Jousting Dummy")).isTapped
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getCard(cards.get("Checkpoint Officer"), 1).isTapped
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Checkpoint Officer"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Checkpoint Officer"))
            gameStatus.getPlayer1().battlefield.search().withName("Checkpoint Officer")
                .cards.get(0).modifiers.setTapped(true)
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Checkpoint Officer"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Jousting Dummy"))
        }
    }
}

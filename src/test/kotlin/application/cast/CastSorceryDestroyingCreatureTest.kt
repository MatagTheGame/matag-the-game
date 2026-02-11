package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CastSorceryDestroyingCreatureTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun castSorceryDestroyingCreature() {
        // When clicking all lands
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Island"), 0).tap()

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()

        // Then card is selected and message ask to choose a target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).isSelected
        browser.player1().statusHelper.hasMessage("Select targets for Legion's Judgment.")

        // When un-selecting the card
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()

        // Then card is unselected
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).isNotSelected
        browser.player1().statusHelper.hasMessage("Play any spell or ability or continue (SPACE).")

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()

        // Then again card is selected and message ask to choose a target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).isSelected
        browser.player1().statusHelper.hasMessage("Select targets for Legion's Judgment.")

        // When clicking a wrong target
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Huatli's Snubhorn")).click()

        // The card is still on the hand selected
        browser.player1().messageHelper.hasMessage("Selected targets were not valid.")
        browser.player1().messageHelper.close()
        browser.player1().messageHelper.hasNoMessage()

        // When clicking again on the sorcery and on a valid target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Colossal Dreadmaw")).click()

        // Then spell goes on the stack
        browser.player1().stackHelper.containsExactly(cards.get("Legion's Judgment"))
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Colossal Dreadmaw")).isTargeted
        browser.player1().statusHelper.hasMessage("Wait for opponent to perform its action...")
        browser.player2().stackHelper.containsExactly(cards.get("Legion's Judgment"))
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Colossal Dreadmaw")).isTargeted
        browser.player2().statusHelper.hasMessage("Play any instant or ability or resolve the top spell in the stack (SPACE).")

        // And priority is passed to the opponent
        browser.player1().actionHelper.cannotContinue()
        browser.player1().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser.player2().actionHelper.canContinue()
        browser.player2().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // When player 2 resolves the spell
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Then the creature is destroyed
        browser.player1().stackHelper.isEmpty
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .doesNotContain(cards.get("Colossal Dreadmaw"))
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Colossal Dreadmaw"))
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Legion's Judgment"))
        browser.player2().stackHelper.isEmpty
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .doesNotContain(cards.get("Colossal Dreadmaw"))
        browser.player2().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Colossal Dreadmaw"))
        browser.player2().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Legion's Judgment"))

        // And priority is passed to the player again
        browser.player1().actionHelper.canContinue()
        browser.player1().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser.player1().statusHelper.hasMessage("Play any spell or ability or continue (SPACE).")
        browser.player2().actionHelper.cannotContinue()
        browser.player2().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser.player2().statusHelper.hasMessage("Wait for opponent to perform its action...")
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Legion's Judgment"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Huatli's Snubhorn"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Colossal Dreadmaw"))
        }
    }
}

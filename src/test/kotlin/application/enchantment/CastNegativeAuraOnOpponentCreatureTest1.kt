package application.enchantment

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
class CastNegativeAuraOnOpponentCreatureTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun castNegativeAuraOnOpponentCreature() {
        // When cast an enchantment aura on a creature with toughness higher than 2
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Swamp"), 0).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Dead Weight")).select()
        browser.player1().statusHelper.hasMessage("Select targets for Dead Weight.")
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).target()

        // Enchantment goes on the stack
        browser.player1().stackHelper.containsExactly(cards.get("Dead Weight"))

        // When opponent accepts enchantment
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Then the attachment and its effect are on the battlefield
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Dead Weight"))
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("1/2")

        // When cast an enchantment aura on a creature with toughness equal 1
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Dead Weight")).select()
        browser.player1().statusHelper.hasMessage("Select targets for Dead Weight.")
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nest Robber")).target()

        // Enchantment goes on the stack
        browser.player1().stackHelper.containsExactly(cards.get("Dead Weight"))

        // When opponent accepts enchantment
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Then the creature immediately dies
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Dead Weight"))
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Nest Robber"))
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Dead Weight"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Dead Weight"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Grazing Whiptail"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Nest Robber"))
        }
    }
}

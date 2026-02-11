package application.enchantment

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

class CastAuraDestroyTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun castAuraDestroy() {
        // Player1 casts 3 auras
        castArcaneFlight(0, "2/4")
        castArcaneFlight(1, "3/5")
        castArcaneFlight(2, "4/6")

        // Player1 continues
        browser.player1().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser.player2().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)

        // When Player2 destroys an enchantment
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Invoke the Divine")).select()
        browser.player2().statusHelper.hasMessage("Select targets for Invoke the Divine.")
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Arcane Flight")).target()
        browser.player1().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)

        // The enchantments and the instant are in the graveyard
        browser.player2().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Invoke the Divine"))
        browser.player2().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Arcane Flight"))
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Concordia Pegasus")).hasPowerAndToughness("3/5")
        browser.player2().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(24)


        // When Player2 destroys the creature
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards.get("Swamp"), 2).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Murder")).select()
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Concordia Pegasus")).target()
        browser.player1().actionHelper.clickContinueAndExpectPhase(BeginCombatPhase.Companion.BC, PlayerType.OPPONENT)

        // Creature and its enchantments are in the graveyard
        browser.player2().getGraveyardHelper(PlayerType.PLAYER)
            .contains(cards.get("Invoke the Divine"), cards.get("Murder"))
        browser.player2().getGraveyardHelper(PlayerType.OPPONENT).contains(
            cards.get("Concordia Pegasus"),
            cards.get("Arcane Flight"),
            cards.get("Arcane Flight"),
            cards.get("Arcane Flight"),
            cards.get("Arcane Flight")
        )
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE).isEmpty
    }

    private fun castArcaneFlight(indexOfLandToTap: Int, powerAndToughness: String?) {
        // When casting Arcane Flight
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .getCard(cards!!.get("Island"), indexOfLandToTap).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Arcane Flight")).select()
        browser.player1().statusHelper.hasMessage("Select targets for Arcane Flight.")
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Concordia Pegasus")).target()

        // Enchantment goes on the stack
        browser.player1().stackHelper.containsExactly(cards.get("Arcane Flight"))

        // When opponent accepts enchantment
        browser.player2().actionHelper.clickContinueAndExpectPhase(Main1Phase.Companion.M1, PlayerType.PLAYER)

        // Then the attachment and its effect are on the battlefield
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Arcane Flight"))
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Concordia Pegasus")).hasPowerAndToughness(powerAndToughness)
    }

    internal class InitTestServiceForTest : InitTestService() {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Arcane Flight"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Arcane Flight"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Arcane Flight"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))

            addCardToNonCurrentPlayerHand(gameStatus, cards.get("Invoke the Divine"))
            addCardToNonCurrentPlayerHand(gameStatus, cards.get("Murder"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
        }
    }
}

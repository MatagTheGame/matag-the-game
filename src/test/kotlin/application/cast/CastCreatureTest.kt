package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.cards.properties.Color
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Import(CastCreatureTest.InitTestServiceForTest::class)
class CastCreatureTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun castCreature() {
        // When click on creature without paying the cost
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Headwater Sentries")).click()

        // Then stack is still empty
        browser.player1().getStackHelper().toHaveSize(0)

        // When clicking all lands
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 0).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 1).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 2).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).click()

        // Then all lands are front-end tapped
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 0).isFrontendTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 1).isFrontendTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 2).isFrontendTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).isFrontendTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).isFrontendTapped()
        browser.player1().getPlayerActiveManaHelper().toHaveMana(
            listOf(
                Color.BLUE,
                Color.BLUE,
                Color.BLUE,
                Color.RED,
                Color.RED
            )
        )

        // When clicking on a land again then gets untapped and when clicking on it again then gets tapped again
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).isNotFrontendTapped()
        browser.player1().getPlayerActiveManaHelper().toHaveMana(
            listOf(
                Color.BLUE,
                Color.BLUE,
                Color.BLUE,
                Color.RED
            )
        )
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).isFrontendTapped()
        browser.player1().getPlayerActiveManaHelper().toHaveMana(
            listOf(
                Color.BLUE,
                Color.BLUE,
                Color.BLUE,
                Color.RED,
                Color.RED
            )
        )

        // When click on creature
        browser.player1().getHandHelper(PlayerType.PLAYER).getCard(cards.get("Headwater Sentries"), 0).click()

        // Then all lands are tapped for both players
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 0).isTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 1).isTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 2).isTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).isTapped()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).isTapped()

        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 0).isTapped()
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 1).isTapped()
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 2).isTapped()
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 0).isTapped()
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).isTapped()

        // And creature is on the stack for both players (hands is empty)
        browser.player1().getStackHelper().containsExactly(cards.get("Headwater Sentries"))
        browser.player1().getHandHelper(PlayerType.PLAYER).isEmpty
        browser.player2().getStackHelper().containsExactly(cards.get("Headwater Sentries"))
        browser.player1().getHandHelper(PlayerType.OPPONENT).isEmpty

        // And priority is to opponent with corresponding status
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
        browser.player1().getActionHelper().cannotContinue()
        browser.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player2().getStatusHelper().hasMessage("Play any instant or ability or resolve the top spell in the stack (SPACE).")
        browser.player2().getActionHelper().canContinue()

        // When player2 acknowledge the spell casted
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Creature goes on the battlefield (stack is empty)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Headwater Sentries"))
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Headwater Sentries"))
        browser.player1().getStackHelper().isEmpty
        browser.player2().getStackHelper().isEmpty

        // And priority is to player again
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).")
        browser.player1().getActionHelper().canContinue()
        browser.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
        browser.player2().getActionHelper().cannotContinue()

        // Hand is now empty
        browser.player1().getHandHelper(PlayerType.PLAYER).isEmpty
        browser.player2().getHandHelper(PlayerType.PLAYER).isEmpty
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Headwater Sentries"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
        }
    }
}

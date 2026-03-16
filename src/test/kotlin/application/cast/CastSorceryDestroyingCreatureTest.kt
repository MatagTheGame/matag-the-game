package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Tag("RegressionTests")
@Import(CastSorceryDestroyingCreatureTest.InitTestServiceForTest::class)
class CastSorceryDestroyingCreatureTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun castSorceryDestroyingCreature() {
        // When clicking all lands
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 0).tap()

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()

        // Then card is selected and message ask to choose a target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).isSelected()
        browser.player1().getStatusHelper().hasMessage("Select targets for Legion's Judgment.")

        // When un-selecting the card
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()

        // Then card is unselected
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).isNotSelected()
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).")

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()

        // Then again card is selected and message ask to choose a target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).isSelected()
        browser.player1().getStatusHelper().hasMessage("Select targets for Legion's Judgment.")

        // When clicking a wrong target
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Huatli's Snubhorn")).click()

        // The card is still on the hand selected
        browser.player1().getMessageHelper().hasMessage("Selected targets were not valid.")
        browser.player1().getMessageHelper().close()
        browser.player1().getMessageHelper().hasNoMessage()

        // When clicking again on the sorcery and on a valid target
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Legion's Judgment")).click()
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Colossal Dreadmaw")).click()

        // Then spell goes on the stack
        browser.player1().getStackHelper().containsExactly(cards.get("Legion's Judgment"))
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Colossal Dreadmaw")).isTargeted()
        browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
        browser.player2().getStackHelper().containsExactly(cards.get("Legion's Judgment"))
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Colossal Dreadmaw")).isTargeted()
        browser.player2().getStatusHelper().hasMessage("Play any instant or ability or resolve the top spell in the stack (SPACE).")

        // And priority is passed to the opponent
        browser.player1().getActionHelper().cannotContinue()
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().canContinue()
        browser.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)

        // When player 2 resolves the spell
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then the creature is destroyed
        browser.player1().getStackHelper().isEmpty
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .doesNotContain(cards.get("Colossal Dreadmaw"))
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Colossal Dreadmaw"))
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Legion's Judgment"))
        browser.player2().getStackHelper().isEmpty
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .doesNotContain(cards.get("Colossal Dreadmaw"))
        browser.player2().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Colossal Dreadmaw"))
        browser.player2().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Legion's Judgment"))

        // And priority is passed to the player again
        browser.player1().getActionHelper().canContinue()
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).")
        browser.player2().getActionHelper().cannotContinue()
        browser.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
    }

    class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initTestGameStatus(gameStatus: GameStatus) {
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

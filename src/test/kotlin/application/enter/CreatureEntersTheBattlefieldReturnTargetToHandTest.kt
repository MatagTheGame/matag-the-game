package application.enter

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test

class CreatureEntersTheBattlefieldReturnTargetToHandTest(
    adminClient: AdminClient,
    gameStatusRepository: GameStatusRepository,
    cardInstanceFactory: CardInstanceFactory,
    cards: Cards,
    private var initService: InitTestService,
) : AbstractApplicationTest(adminClient, gameStatusRepository, cardInstanceFactory, cards) {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun creatureEntersTheBattlefieldReturnTargetToHand() {
        // When Exclusion Mage
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards!!.get("Island"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Exclusion Mage")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then Exclusion Mage is on the battlefield and its trigger on the stack
        val exclusionMageId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Exclusion Mage")).getCardIdNumeric()
        browser.player1().getStackHelper().containsAbility("Player1's Exclusion Mage (" + exclusionMageId + "): That targets get returned to its owner's hand.")

        // When player 1 selects opponent creature to return
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Banehound")).target()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then it's returned to its owner hand
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE).isEmpty
        browser.player1().getHandHelper(PlayerType.OPPONENT).toHaveSize(1)

        // When playing another Exclusion Mage
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 3).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 4).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Island"), 5).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Exclusion Mage")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Player 1 continue without targets as nothing can be targeted
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player1().getStackHelper().isEmpty
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Exclusion Mage"), cards.get("Exclusion Mage"))

        // Moving to player 1 main phase
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main2Phase.M2, PlayerType.PLAYER)
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)

        // Replaying Banehound
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getFirstCard(cards.get("Swamp")).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Banehound")).click()
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Banehound"))
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Exclusion Mage"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Exclusion Mage"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"))

            addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Banehound"))
        }
    }
}

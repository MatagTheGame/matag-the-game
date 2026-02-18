package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test

class WheneverCastTest(
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
    fun wheneverACreatureEntersTheBattlefieldAbility() {
        // When playing Precision Bolt
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards!!.get("Mountain"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Mountain"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Precision Bolt")).select()
        browser.player1().getPlayerInfoHelper(PlayerType.OPPONENT).click()

        // Then Adeliz gets activated
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.OPPONENT)
        val adelizId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Adeliz, the Cinder Wind")).getCardIdNumeric()
        browser.player1().getStackHelper().containsAbility("Player1's Adeliz, the Cinder Wind (" + adelizId + "): Wizards you control get +1/+1 until end of turn.")

        // When Adeliz triggered ability resolves
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)

        // Then Adeliz power is increased
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Adeliz, the Cinder Wind")).hasPowerAndToughness("3/3")

        // When Precision Bolt
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Opponent loses 3 life
        browser.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(17)
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Adeliz, the Cinder Wind"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Precision Bolt"))
        }
    }
}

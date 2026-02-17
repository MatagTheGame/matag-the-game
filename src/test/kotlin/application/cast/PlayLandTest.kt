package application.cast

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import application.cast.CastCreatureAlternativeCostTest.InitTestServiceForTest
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PlayLandTest(
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
    fun playLand() {
        // When play first land
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards!!.get("Island")).click()

        // Then battlefields contain land
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .containsExactly(cards.get("Island"))
        browser.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.FIRST_LINE)
            .containsExactly(cards.get("Island"))

        // Hand is empty
        browser.player1().getHandHelper(PlayerType.PLAYER).toHaveSize(1)
        browser.player2().getHandHelper(PlayerType.OPPONENT).toHaveSize(1)

        // When play second land
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Island")).click()

        // Then error is displayed
        browser.player1().getMessageHelper().hasMessage("You already played a land this turn.")
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"))
        }
    }
}

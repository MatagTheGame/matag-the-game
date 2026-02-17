package application.enter

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import application.cast.CastCreatureAlternativeCostTest.InitTestServiceForTest
import com.matag.cards.Cards
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Tag("RegressionTests")
class CreatureEntersTheBattlefieldDoubleAbilityTest(
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
    fun creatureEntersTheBattlefieldDoubleAbility() {
        // When Playing Dusk Legion Zealot
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards!!.get("Swamp"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Dusk Legion Zealot")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)
        val duskLegionZealotId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Dusk Legion Zealot")).getCardIdNumeric()
        browser.player1().getStackHelper().containsAbility("Player1's Dusk Legion Zealot (" + duskLegionZealotId + "): You draw 1 card and lose 1 life.")
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then both abilities happen
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Dusk Legion Zealot"))
        browser.player1().getHandHelper(PlayerType.PLAYER).containsExactly(cards.get("Swamp"))
        browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(19)
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Dusk Legion Zealot"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerLibrary(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerLibrary(gameStatus, cards.get("Swamp"))
        }
    }
}
